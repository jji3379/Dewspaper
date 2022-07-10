package backend.ssr.ddd.ssrblog.account.dto.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountProfileResponse {
    private Long accountIdx;
    private String profileId; //SHA-256
    private String email;
    private String platform;
    private String name;
    private String blogName;
    private String role;
    private String profileImg;
    private String introduction;
    private String emailAgree;
    private String alarmAgree;
    private boolean owner;
    private long postCount;
    private long commentCount;
    private long crewCount;

    private boolean isCrew;

    @Builder
    public AccountProfileResponse(Long accountIdx, String profileId, String email, String platform, String name, String blogName, String role, String profileImg, String introduction, String emailAgree, String alarmAgree, boolean owner, long postCount, long commentCount, long crewCount, boolean isCrew) {
        this.accountIdx = accountIdx;
        this.profileId = profileId;
        this.email = email;
        this.platform = platform;
        this.name = name;
        this.blogName = blogName;
        this.role = role;
        this.profileImg = profileImg;
        this.introduction = introduction;
        this.emailAgree = emailAgree;
        this.alarmAgree = alarmAgree;
        this.owner = owner;
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.crewCount = crewCount;
        this.isCrew = isCrew;
    }
}
