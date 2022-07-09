package backend.ssr.ddd.ssrblog.account.dto.profile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountProfileRequest {
    @ApiModelProperty(value = "회원의 이름", example = "DDD", position = 1)
    private String name;

    @ApiModelProperty(value = "프로필 이미지", example = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIn1iNdqod_ukBY45LqZNCn89wDQMsfxhncg&usqp=CAU", position = 2)
    private String profileImg;

    @ApiModelProperty(value = "작가 소개", example = "안녕하세요! 저는 DDD 의 서버 개발자 DDD 입니다.", position = 3)
    private String introduction;

    @ApiModelProperty(value = "이메일 수신 동의", example = "N", position = 4)
    private String emailAg;

    @ApiModelProperty(value = "알림 동의", example = "N", position = 5)
    private String alarmAg;

    @Builder
    public AccountProfileRequest(String name, String profileImg, String introduction, String emailAg, String alarmAg) {
        this.name = name;
        this.profileImg = profileImg;
        this.introduction = introduction;
        this.emailAg = emailAg;
        this.alarmAg = alarmAg;
    }
}
