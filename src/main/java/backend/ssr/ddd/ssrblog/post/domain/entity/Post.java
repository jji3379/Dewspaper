package backend.ssr.ddd.ssrblog.post.domain.entity;

import backend.ssr.ddd.ssrblog.common.TimeEntity.BaseTimeEntity;
import backend.ssr.ddd.ssrblog.post.dto.PostRequest;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIdx;

    private String thumbnailImg;

    private String thumbnailContents;

    private String title;

    private String contents;

    private int boardCount;

    private String privated;

    private String delYn = "N";

    public void update(PostRequest postRequest) {
        this.thumbnailImg = postRequest.getThumbnailImg();
        this.thumbnailContents = postRequest.getThumbnailContents();
        this.title = postRequest.getTitle();
        this.contents = postRequest.getContents();
    }

    public void updateBoardCount() {
        this.boardCount++;
    }

    public void delete() {
        this.delYn = "Y";
    }

    @Builder
    public Post(Long postIdx, String thumbnailImg, String thumbnailContents, String title, String contents, int boardCount, String privated, String delYn) {
        this.postIdx = postIdx;
        this.thumbnailImg = thumbnailImg;
        this.thumbnailContents = thumbnailContents;
        this.title = title;
        this.contents = contents;
        this.boardCount = boardCount;
        this.privated = privated;
        this.delYn = delYn;
    }

    public PostResponse toResponse() {
        PostResponse build = PostResponse.builder()
                .postIdx(postIdx)
                .thumbnailImg(thumbnailImg)
                .thumbnailContents(thumbnailContents)
                .title(title)
                .contents(contents)
                .boardCount(boardCount)
                .privated(privated)
                .delYn(delYn)
                .createDate(getCreateDate())
                .updateDate(getUpdateDate())
                .build();

        return build;
    }
}
