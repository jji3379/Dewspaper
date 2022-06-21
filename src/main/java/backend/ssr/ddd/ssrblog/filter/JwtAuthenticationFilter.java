package backend.ssr.ddd.ssrblog.filter;

import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        log.info("resolveToken : {}", token);
        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            if (StringUtils.isEmpty(token)) {
                request.setAttribute("unauthorization", "401 인증키 없음.");
                log.debug("인증키 없음");
            }

            if (jwtTokenProvider.validateToken(token)) {
                request.setAttribute("unauthorization", "401 인증키 만료.");
                log.debug("인증키 만료");
            }
        }
        chain.doFilter(request, response);
    }
}
