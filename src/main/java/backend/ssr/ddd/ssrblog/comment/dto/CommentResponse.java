package backend.ssr.ddd.ssrblog.comment.dto;

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
    private Long accountIdx;

    @ApiModelProperty(value = "댓글 내용", example = "살려줘", position = 4)
    private String comment;

    @ApiModelProperty(value = "작성 일시", example = "2022-06-28 03:01:35", position = 5)
    private LocalDateTime dateTime;

    @Builder
    public CommentResponse(Long commentIdx, Long postIdx, Long accountIdx, String comment, LocalDateTime dateTime) {
        this.commentIdx = commentIdx;
        this.postIdx = postIdx;
        this.accountIdx = accountIdx;
        this.comment = comment;
        this.dateTime = dateTime;
    }
}
