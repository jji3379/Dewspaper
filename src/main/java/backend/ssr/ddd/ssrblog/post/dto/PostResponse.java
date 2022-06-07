package backend.ssr.ddd.ssrblog.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponse {

    private Long postIdx;

    private String thumnail_img;

    private String thumnail_contents;

    private String title;

    private String contents;

    private int board_count;

    private String privated;

    private String deleted;

    private LocalDateTime dateTime;

    @Builder
    public PostResponse(Long postIdx, String thumnail_img, String thumnail_contents, String title, String contents, int board_count, String privated, String deleted, LocalDateTime dateTime) {
        this.postIdx = postIdx;
        this.thumnail_img = thumnail_img;
        this.thumnail_contents = thumnail_contents;
        this.title = title;
        this.contents = contents;
        this.board_count = board_count;
        this.privated = privated;
        this.deleted = deleted;
        this.dateTime = dateTime;
    }
}