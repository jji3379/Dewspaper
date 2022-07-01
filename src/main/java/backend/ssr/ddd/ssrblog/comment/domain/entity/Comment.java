package backend.ssr.ddd.ssrblog.comment.domain.entity;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.comment.dto.CommentRequest;
import backend.ssr.ddd.ssrblog.comment.dto.CommentResponse;
import backend.ssr.ddd.ssrblog.common.TimeEntity.BaseTimeEntity;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx")
    private Post postIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_idx")
    private Account accountIdx;

    private String comment;

    @Builder
    public Comment(Long commentIdx, Post postIdx, Account accountIdx, String comment) {
        this.commentIdx = commentIdx;
        this.postIdx = postIdx;
        this.accountIdx = accountIdx;
        this.comment = comment;
    }

    public void update(CommentRequest commentRequest) {
        this.comment = commentRequest.getComment();
    }

    public CommentResponse toResponse() {
        CommentResponse build = CommentResponse.builder()
                .postIdx(postIdx.getPostIdx())
                .commentIdx(commentIdx)
                .accountIdx(accountIdx.toResponse())
                .comment(comment)
                .createDate(getCreateDate())
                .updateDate(getUpdateDate())
                .build();

        return build;
    }
}
