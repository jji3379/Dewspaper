package backend.ssr.ddd.ssrblog.friends.dto;

import backend.ssr.ddd.ssrblog.account.dto.friends.AccountFriendsResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class FriendsNoticeResponse {
    @ApiModelProperty(value = "신청 회원", example = "8", position = 1)
    private AccountFriendsResponse requesterIdx;

    @ApiModelProperty(value = "수락 회원", example = "11", position = 2)
    private AccountFriendsResponse accepterIdx;

    @ApiModelProperty(value = "수락 여부", example = "W", position = 3)
    private String accepted;

    @ApiModelProperty(value = "요청자 알림 확인 여부", example = "N", position = 4)
    private String requesterNoticeCheckYn = "N";

    @ApiModelProperty(value = "요청자 알림 삭제 여부", example = "N", position = 5)
    private String requesterNoticeDelYn = "N";

    @ApiModelProperty(value = "수락자 알림 확인 여부", example = "N", position = 6)
    private String accepterNoticeCheckYn = "N";

    @ApiModelProperty(value = "수락자 알림 삭제 여부", example = "N", position = 7)
    private String accepterNoticeDelYn = "N";
    @CreatedDate
    @ApiModelProperty(value = "요청 날짜", example = "2022-06-21 01:52:37", position = 8)
    private LocalDateTime requestDateTime;

    @LastModifiedDate
    @ApiModelProperty(value = "수락 날짜", example = "2022-06-22 01:52:37", position = 9)
    private LocalDateTime acceptedDateTime;

    @Builder
    public FriendsNoticeResponse(AccountFriendsResponse requesterIdx, AccountFriendsResponse accepterIdx, String accepted, String requesterNoticeCheckYn, String requesterNoticeDelYn, String accepterNoticeCheckYn, String accepterNoticeDelYn, LocalDateTime requestDateTime, LocalDateTime acceptedDateTime) {
        this.requesterIdx = requesterIdx;
        this.accepterIdx = accepterIdx;
        this.accepted = accepted;
        this.requesterNoticeCheckYn = requesterNoticeCheckYn;
        this.requesterNoticeDelYn = requesterNoticeDelYn;
        this.accepterNoticeCheckYn = accepterNoticeCheckYn;
        this.accepterNoticeDelYn = accepterNoticeDelYn;
        this.requestDateTime = requestDateTime;
        this.acceptedDateTime = acceptedDateTime;
    }
}
