package backend.ssr.ddd.ssrblog.filter;

import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            if (StringUtils.isEmpty(token) || token == null) {
                request.setAttribute("unauthorization", "401 인증키 없음.");

                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 존재하지 않습니다.");
            }

            if (!jwtTokenProvider.validateToken(token)) {
                request.setAttribute("unauthorization", "401 인증키 만료.");

                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료 되었습니다.");
            }
        }
        chain.doFilter(request, response);
    }
}
