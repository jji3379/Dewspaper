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

    private String thumnail_img;

    private String thumnail_contents;

    private String title;

    private String contents;

    private int board_count;

    private String privated;

    private String deleted;

    public void update(PostRequest postRequest) {
        this.thumnail_img = postRequest.getThumnail_img();
        this.thumnail_contents = postRequest.getThumnail_contents();
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
    public Post(Long postIdx, String thumnail_img, String thumnail_contents, String title, String contents, int board_count, String privated, String deleted) {
        this.postIdx = postIdx;
        this.thumnail_img = thumnail_img;
        this.thumnail_contents = thumnail_contents;
        this.title = title;
        this.contents = contents;
        this.board_count = board_count;
        this.privated = privated;
        this.deleted = deleted;
    }

    public PostResponse toResponse() {
        PostResponse build = PostResponse.builder()
                .postIdx(postIdx)
                .thumnail_img(thumnail_img)
                .thumnail_contents(thumnail_contents)
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
