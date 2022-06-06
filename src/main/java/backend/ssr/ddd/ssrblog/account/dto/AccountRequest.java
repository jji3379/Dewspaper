package backend.ssr.ddd.ssrblog.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccountRequest {
    private String email;
    private String name;
    private String picture;
    private String platform;
    private String role;

    @Builder
    public AccountRequest(String email, String name, String picture, String platform, String role) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.platform = platform;
        this.role = role;
    }
}
