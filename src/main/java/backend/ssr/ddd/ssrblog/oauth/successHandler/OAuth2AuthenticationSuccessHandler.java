package backend.ssr.ddd.ssrblog.oauth.successHandler;

import backend.ssr.ddd.ssrblog.account.dto.AccountRequest;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtResponse;
import backend.ssr.ddd.ssrblog.oauth.mapper.AccountRequestMapper;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRequestMapper accountRequestMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        //login 성공한 사용자 목록.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        AccountRequest accountRequest = accountRequestMapper.toDto(oAuth2User);

        String accessToken = jwtTokenProvider.createToken(accountRequest.getEmail(), accountRequest.getRole(), accountRequest.getPlatform());
        String refreshToken = jwtTokenProvider.createRefreshToken(accountRequest.getEmail(), accountRequest.getPlatform());

        JwtResponse jwt = new JwtResponse();
        jwt.createJwt(accessToken, refreshToken);

        log.info("email : {}, jwt : {}",accountRequest.getEmail(), jwt.getAccessToken());
        log.info("email : {}, refresh-token : {}",accountRequest.getEmail(), jwt.getRefreshToken());

        String url = makeRedirectUrl(jwt);

        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(JwtResponse jwt) {

        return UriComponentsBuilder.fromUriString("http://localhost:3000/callback/login?token=" + jwt.getAccessToken() + "&refreshtoken=" + jwt.getRefreshToken())
                .build().toUriString();
    }
}
