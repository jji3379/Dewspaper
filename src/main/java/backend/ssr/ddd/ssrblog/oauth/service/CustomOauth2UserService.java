package backend.ssr.ddd.ssrblog.oauth.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
/**
 * DefaultOAuth2UserService를 상속한 UserOAuth2Service 클래스 구현.
 * OAuth2UserService 클래스는 사용자의 정보들을 기반으로 가입 및 정보수정, 세션 저장등의 기능을 지원해준다.
 * DefaultOAuth2UserService 클래스 안의 loadUser 메서드를 호출되게 했고, 이걸 활용해서 회원가입 작업을 한다.
 */
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    /**
     * - access token을 이용해 서드파티 서버로부터 사용자 정보를 받아온다.
     *
     * - 해당 사용자가 이미 회원가입 되어있는 사용자인지 확인한다.
     * 만약 회원가입이 되어있지 않다면, 회원가입 처리한다.
     * 만약 회원가입이 되어있다면 가입한 적 있다는 log를 찍고 로그인한다. (추후 가입한 적 있다는 alret 추가로 구현하면 좋을듯)
     *
     * - UserPrincipal 을 return 한다. 세션 방식에서는 여기서 return한 객체가 시큐리티 세션에 저장된다.
     * 하지만 JWT 방식에서는 저장하지 않는다. (JWT 방식에서는 인증&인가 수행시 HttpSession을 사용하지 않을 것이다.)
     */
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // loadUser(OAuth2UserRequest oAuth2UserRequest) 메서드는 사용자 정보를 요청할 수 있는 access token 을 얻고나서 실행된다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // 여기서 access token을 이용해 서버로부터 사용자 정보를 받아온다.

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute attributes = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Map<String, Object> customAttributes = new HashMap<>(); // attributes 에 platform 정보를 넣기 위함
        Map<String, Object> attributes1 = attributes.getAttributes(); // attributes 가 불변 리스트라 여기에 추가 불가능

        Account account = saveOrUpdate(attributes); // DB 에 회원 정보를 저장 또는 수정하는 코드

        customAttributes.putAll(attributes1);
        customAttributes.put("platform", registrationId);
        customAttributes.put("role", account.getRole().getRole());

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(account.getRoleKey())), customAttributes, attributes.getAttributeKey());
    }

    private Account saveOrUpdate(OAuth2Attribute attributes) {
        Account account = accountRepository.findByEmailAndPlatform(attributes.getEmail(), attributes.getPlatform())
                .map(entity -> entity.update(attributes.getPlatform(), attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return account;
    }
}