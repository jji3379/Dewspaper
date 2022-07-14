package backend.ssr.ddd.ssrblog.account.dto.friends;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AccountFriendsResponse {
    private Long accountIdx;
    private String name;
    private String profileImg;

    @Builder
    public AccountFriendsResponse(Long accountIdx, String name, String profileImg) {
        this.accountIdx = accountIdx;
        this.name = name;
        this.profileImg = profileImg;
    }
}
