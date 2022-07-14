package backend.ssr.ddd.ssrblog.friends.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ApiModel(description = "친구 요청 응답 정보")
public class FriendsResponse {

    @ApiModelProperty(value = "신청 회원", example = "8", position = 1)
    private Long requesterIdx;

    @ApiModelProperty(value = "수락 회원", example = "11", position = 2)
    private Long accepterIdx;

    @ApiModelProperty(value = "수락 여부", example = "W", position = 3)
    private String accepted;

    @ApiModelProperty(value = "알림 확인 여부", example = "N", position = 4)
    private String noticeCheckYn;

    @ApiModelProperty(value = "알림 삭제 여부", example = "N", position = 5)
    private String noticeDelYn;
    @CreatedDate
    @ApiModelProperty(value = "요청 날짜", example = "2022-06-21 01:52:37", position = 6)
    private LocalDateTime requestDateTime;

    @LastModifiedDate
    @ApiModelProperty(value = "수락 날짜", example = "2022-06-22 01:52:37", position = 7)
    private LocalDateTime acceptedDateTime;

    @Builder
    public FriendsResponse(Long requesterIdx, Long accepterIdx, String accepted, String noticeCheckYn, String noticeDelYn, LocalDateTime requestDateTime, LocalDateTime acceptedDateTime) {
        this.requesterIdx = requesterIdx;
        this.accepterIdx = accepterIdx;
        this.accepted = accepted;
        this.noticeCheckYn = noticeCheckYn;
        this.noticeDelYn = noticeDelYn;
        this.requestDateTime = requestDateTime;
        this.acceptedDateTime = acceptedDateTime;
    }
}
