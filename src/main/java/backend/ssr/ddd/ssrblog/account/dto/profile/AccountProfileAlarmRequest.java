package backend.ssr.ddd.ssrblog.account.dto.profile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountProfileAlarmRequest {

    @ApiModelProperty(value = "이메일 수신 동의", example = "N", position = 5)
    private String emailAgree;

    @ApiModelProperty(value = "알림 동의", example = "N", position = 6)
    private String alarmAgree;

    @Builder
    public AccountProfileAlarmRequest(String emailAgree, String alarmAgree) {
        this.emailAgree = emailAgree;
        this.alarmAgree = alarmAgree;
    }
}
