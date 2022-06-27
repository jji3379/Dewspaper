package backend.ssr.ddd.ssrblog.account.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountMeResponse {
    private String name;
    private String profileImg;

    @Builder
    public AccountMeResponse(String name, String profileImg) {
        this.name = name;
        this.profileImg = profileImg;
    }
}
