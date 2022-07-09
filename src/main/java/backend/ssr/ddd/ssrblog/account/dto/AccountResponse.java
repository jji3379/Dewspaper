package backend.ssr.ddd.ssrblog.account.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class AccountResponse {
    private Long accountIdx;
    private String email;
    private String platform;
    private String name;
    private String role;
    private String profileImg;
    private String withdrawal;
    private String introduction;
    private String emailAg;
    private String alarmAg;
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @Builder
    public AccountResponse(Long accountIdx, String email, String platform, String name, String role, String profileImg, String withdrawal, String introduction, String emailAg, String alarmAg, LocalDateTime createDate, LocalDateTime updateDate) {
        this.accountIdx = accountIdx;
        this.email = email;
        this.platform = platform;
        this.name = name;
        this.role = role;
        this.profileImg = profileImg;
        this.withdrawal = withdrawal;
        this.introduction = introduction;
        this.emailAg = emailAg;
        this.alarmAg = alarmAg;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
