package backend.ssr.ddd.ssrblog.oauth;

import backend.ssr.ddd.ssrblog.domain.entity.Account;
import backend.ssr.ddd.ssrblog.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final AccountRepository accountRepository;
    private final HttpSession httpSession;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        //Account account = saveOrUpdate(attributes);

        //httpSession.setAttribute("acoount", new SessionAccount(account));

        //return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(account.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey());

        System.out.println("getClientRegistration : "+userRequest.getClientRegistration());
        System.out.println("getClientRegistrationId : "+userRequest.getClientRegistration().getRegistrationId());
        System.out.println("tokenValue : "+userRequest.getAccessToken().getTokenValue());
        System.out.println("tokenExpire : "+userRequest.getAccessToken().getExpiresAt());
        System.out.println("tokenIssue : "+userRequest.getAccessToken().getIssuedAt());
        System.out.println("tokenType : "+userRequest.getAccessToken().getTokenType());
        System.out.println("tokenScope : "+userRequest.getAccessToken().getScopes());

        System.out.println("getAttributes : "+super.loadUser(userRequest).getAttributes());

        return super.loadUser(userRequest);
    }

    private Account saveOrUpdate(OAuthAttributes attributes) {
        Account account = accountRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return accountRepository.save(account);
    }
}