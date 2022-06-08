package backend.ssr.ddd.ssrblog.post.domain.entity;

import backend.ssr.ddd.ssrblog.common.BaseTimeEntity;
import backend.ssr.ddd.ssrblog.post.dto.PostRequest;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIdx;

    private String thumnailImg;

    private String thumnailContents;

    private String title;

    private String contents;

    private int board_count;

    private String privated;

    private String deleted;

    public void update(PostRequest postRequest) {
        this.thumnailImg = postRequest.getThumnailImg();
        this.thumnailContents = postRequest.getThumnailContents();
        this.title = postRequest.getTitle();
        this.contents = postRequest.getContents();
    }

    public void updateBoardCount() {
        this.board_count++;
    }

    public void delete() {
        this.deleted = "Y";
    }

    @Builder
    public Post(Long postIdx, String thumnailImg, String thumnailContents, String title, String contents, int board_count, String privated, String deleted) {
        this.postIdx = postIdx;
        this.thumnailImg = thumnailImg;
        this.thumnailContents = thumnailContents;
        this.title = title;
        this.contents = contents;
        this.board_count = board_count;
        this.privated = privated;
        this.deleted = deleted;
    }

    public PostResponse toResponse() {
        PostResponse build = PostResponse.builder()
                .postIdx(postIdx)
                .thumnailImg(thumnailImg)
                .thumnailContents(thumnailContents)
                .title(title)
                .contents(contents)
                .board_count(board_count)
                .privated(privated)
                .deleted(deleted)
                .dateTime(getDateTime())
                .build();

        return build;
    }
}
