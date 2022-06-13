package backend.ssr.ddd.ssrblog.oauth.successHandler;

import backend.ssr.ddd.ssrblog.account.dto.AccountRequest;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtResponse;
import backend.ssr.ddd.ssrblog.oauth.mapper.AccountRequestMapper;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

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

        JwtResponse jwt = jwtTokenProvider.createToken(accountRequest.getEmail(), accountRequest.getRole(), accountRequest.getPlatform());

        log.info("jwt : {}", jwt.getAccessToken());

        String url = makeRedirectUrl(jwt, response);
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(JwtResponse jwt, HttpServletResponse response) throws IOException {
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("Token", jwt.getAccessToken());
//        body.put("refreshToken", jwt.getRefreshToken());
//
//        response.setStatus(HttpStatus.OK.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        new ObjectMapper().writeValue(response.getOutputStream(), body);

        return UriComponentsBuilder.fromUriString("http://localhost:3000/callback/login/"+ jwt.getAccessToken())
                .build().toUriString();
    }
}
