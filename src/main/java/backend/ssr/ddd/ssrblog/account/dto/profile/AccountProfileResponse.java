package backend.ssr.ddd.ssrblog.account.dto.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountProfileResponse {
    private Long accountIdx;
    private String profileId; //uuid
    private String email;
    private String platform;
    private String name;
    private String role;
    private String profileImg;
    private String introduction;
    private String emailAg;
    private String alarmAg;

    private boolean owner;
    private long postCount;
    private long commentCount;
    private long crewCount;

    @Builder
    public AccountProfileResponse(Long accountIdx, String profileId, String email, String platform, String name, String role, String profileImg, String introduction, String emailAg, String alarmAg, boolean owner, long postCount, long commentCount, long crewCount) {
        this.accountIdx = accountIdx;
        this.profileId = profileId;
        this.email = email;
        this.platform = platform;
        this.name = name;
        this.role = role;
        this.profileImg = profileImg;
        this.introduction = introduction;
        this.emailAg = emailAg;
        this.alarmAg = alarmAg;
        this.owner = owner;
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.crewCount = crewCount;
    }
}
