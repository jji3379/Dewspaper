package backend.ssr.ddd.ssrblog.oauth.jwt;


import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtResponse {
    private String grantType; // bearer
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;

    @Builder
    public JwtResponse(String grantType, String accessToken, String refreshToken, Long accessTokenExpireDate) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireDate = accessTokenExpireDate;
    }
}
