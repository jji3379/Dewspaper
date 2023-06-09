package backend.ssr.ddd.ssrblog.config.security;

import backend.ssr.ddd.ssrblog.filter.JwtAuthenticationFilter;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtAccessDeniedHandler;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtAuthenticationEntryPoint;
import backend.ssr.ddd.ssrblog.oauth.service.CustomOauth2UserService;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import backend.ssr.ddd.ssrblog.oauth.service.CustomUserDetailService;
import backend.ssr.ddd.ssrblog.oauth.successHandler.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOauth2UserService customOauth2UserService;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 X
                .and()
                    .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
                .antMatchers("/api/test").authenticated()
                .antMatchers("/api/me").authenticated()
                .anyRequest().permitAll()
//                        .antMatchers("/api/**").authenticated()
//                        .antMatchers(HttpMethod.POST).authenticated() // 등록에 대한 Method 에 접근시 인증된 사용자만 접근 가능
//                        .antMatchers(HttpMethod.PUT).authenticated() // 수정에 대한 Method 에 접근시 인증된 사용자만 접근 가능
//                        .antMatchers(HttpMethod.DELETE).authenticated() // 삭제에 대한 Method 에 접근시 인증된 사용자만 접근 가능
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                    .oauth2Login() // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        .successHandler(oAuth2AuthenticationSuccessHandler) // 인증 프로세스에 따라 사용자 정의 로직 실행
                            .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당한다.
                                .userService(customOauth2UserService); // 로그인이 성공하면 해당 유저의 정보를 들고, customOAuth2UserService 에서 후처리를 해주겠다는 의미
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(customUserDetailService);
    }
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {

        return super.authenticationManagerBean();
    }

    // CORS 허용 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
