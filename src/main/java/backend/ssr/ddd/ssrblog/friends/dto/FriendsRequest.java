package backend.ssr.ddd.ssrblog.friends.dto;


import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(description = "친구 요청 정보")
public class FriendsRequest {

    @NotBlank(message = "신청 회원은 필수 입력 항목입니다.")
    @ApiModelProperty(value = "신청 회원", required = true, example = "8", position = 1)
    private Long requesterIdx;

    @NotBlank(message = "수락 회원은 필수 입력 항목입니다.")
    @ApiModelProperty(value = "수락 회원", required = true, example = "21", position = 2)
    private Long accepterIdx;

    @Builder
    public FriendsRequest(Long requesterIdx, Long accepterIdx) {
        this.requesterIdx = requesterIdx;
        this.accepterIdx = accepterIdx;
    }

    public Friends toEntity() {
        Friends build = Friends.builder()
                .requesterIdx(Account.builder().accountIdx(requesterIdx).build())
                .accepterIdx(Account.builder().accountIdx(accepterIdx).build())
                .accepted("N")
                .build();

        return build;
    }

    public Account getRequesterIdx() {
        return Account.builder().accountIdx(requesterIdx).build();
    }

    public Account getAccepterIdx() {
        return Account.builder().accountIdx(accepterIdx).build();
    }
}
