package backend.ssr.ddd.ssrblog.oauth.jwt;


import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtResponse {
    private String accessToken;
    private String refreshToken;

    public JwtResponse createJwt(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

        return this;
    }
}
