package backend.ssr.ddd.ssrblog.comment.dto;

import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ApiModel(description = "댓글 응답 정보")
public class CommentResponse {
    @ApiModelProperty(value = "댓글 번호", example = "1", position = 1)
    private Long commentIdx;

    @ApiModelProperty(value = "게시물 번호", example = "1", position = 2)
    private Long postIdx;

    @ApiModelProperty(value = "회원 번호", example = "8", position = 3)
    private AccountResponse account;

    @ApiModelProperty(value = "댓글 내용", example = "살려줘", position = 4)
    private String comment;

    @ApiModelProperty(value = "작성 일시", example = "2022-06-28 03:01:35", position = 5)
    private LocalDateTime createDate;

    @ApiModelProperty(value = "수정 일시", example = "2022-06-28 03:01:35", position = 6)
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "삭제 여부", example = "N", position = 7)
    private String delYn;

    @Builder
    public CommentResponse(Long commentIdx, Long postIdx, AccountResponse account, String comment, LocalDateTime createDate, LocalDateTime updateDate) {
        this.commentIdx = commentIdx;
        this.postIdx = postIdx;
        this.account = account;
        this.comment = comment;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
