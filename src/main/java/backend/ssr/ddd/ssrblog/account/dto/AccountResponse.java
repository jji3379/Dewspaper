package backend.ssr.ddd.ssrblog.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AccountResponse {
    private Long accountIdx;
    private String email;
    private String platform;
    private String name;
    private String role;
    private String profileImg;
    private String withdrawal;

    @Builder
    public AccountResponse(Long accountIdx, String profileImg, String platform, String email, String name, String role, String withdrawal) {
        this.accountIdx = accountIdx;
        this.email = email;
        this.platform = platform;
        this.name = name;
        this.role = role;
        this.profileImg = profileImg;
        this.withdrawal = withdrawal;
    }
}
