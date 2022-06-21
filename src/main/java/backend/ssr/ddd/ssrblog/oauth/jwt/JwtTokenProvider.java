package backend.ssr.ddd.ssrblog.oauth.jwt;

import backend.ssr.ddd.ssrblog.oauth.service.CustomUserDetailService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    @Value("spring.jwt.secret")
    private String secretKey ;

    private final CustomUserDetailService customUserDetailService;

//    private Long tokenValidMilisecond = 1000L * 60 * 60; // 1시간만 토큰 유효
//    private Long refreshValidMilisecond = 1000L * 60 * 60 * 24 * 7; // 7일 토큰 유효

    private Long tokenValidMilisecond = 1000L * 10; // 프론트 테스트 10초
    private Long refreshValidMilisecond = 1000L * 20; // 프론트 테스트 20초

    @PostConstruct
    protected void init() {
        secretKey  = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
    public String createToken(String email, String roles, String platform) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuer(platform) // 발급한 플랫폼
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey ) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    public String createRefreshToken(String email, String platform) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(platform)
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + refreshValidMilisecond)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey ) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        // CustomUserDetailsService 가 UserDetailsService 를 상속받고 있고 security auth config 에서 사용되기에 username 안에 email 과 platfrom 정보를 담는다.
        String username = this.getUserEmail(token) + "," + this.getUserPlatform(token);

        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 회원 정보의 플랫폼
    public String getUserPlatform(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getIssuer();
    }

    // Request의 Header에서 token 값을 가져옵니다. "authorization" : "token"
    public String resolveToken(HttpServletRequest request) {
        if(request.getHeader("Authorization") != null )
            return request.getHeader("Authorization").substring(7);
        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            //log.error("JWT claims string is empty.");
        }
        return false;
    }

}
