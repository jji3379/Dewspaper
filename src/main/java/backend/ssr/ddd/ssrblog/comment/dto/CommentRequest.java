package backend.ssr.ddd.ssrblog.comment.dto;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.comment.domain.entity.Comment;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "댓글 요청 정보")
public class CommentRequest {

    @NotBlank(message = "댓글 작성자는 필수 입력 항목입니다.")
    @ApiModelProperty(value = "댓글 작성자", required = true, example = "8", position = 1)
    private Long accountIdx;

    @NotBlank(message = "댓글 내용은 필수 입력 항목입니다.")
    @Size(max = 1000, message = "댓글 내용은 최대 1000자까지 입력 가능합니다.")
    @ApiModelProperty(value = "댓글 내용", required = true, example = "밤이 되었습니다. 서버 개발자는 고개를 들어주세요...", position = 1)
    private String comment;

    public Comment toEntity(Long postIdx) {
        Comment build = Comment.builder()
                .postIdx(Post.builder().postIdx(postIdx).build())
                .accountIdx(Account.builder().accountIdx(accountIdx).build())
                .comment(comment)
                .build();

        return build;
    }
}
