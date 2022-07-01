package backend.ssr.ddd.ssrblog.post.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ApiModel(description = "게시물 응답 정보")
public class PostResponse {

    @ApiModelProperty(value = "게시물 번호", example = "1", position = 1)
    private Long postIdx;

    @ApiModelProperty(value = "게시물 제목", example = "server-side rendering", position = 2)
    private String title;

    @ApiModelProperty(value = "게시물 내용", example = "server-side rendering 은 ~~", position = 3)
    private String contents;

    @ApiModelProperty(value = "썸네일 이미지", example = "http://upload2.inven.co.kr/upload/2019/12/27/bbs/i14210693079.jpg", position = 4)
    private String thumbnailImg;

    @ApiModelProperty(value = "썸네일 내용", example = "server-side rendering 은", position = 5)
    private String thumbnailContents;

    @ApiModelProperty(value = "조회수", example = "15", position = 6)
    private int boardCount;

    @ApiModelProperty(value = "비공개 여부", example = "N", position = 7)
    private String privated;

    @ApiModelProperty(value = "작성 일시", example = "2022-06-28 03:01:35", position = 8)
    private LocalDateTime createDate;

    @ApiModelProperty(value = "수정 일시", example = "2022-06-28 03:01:35", position = 9)
    private LocalDateTime updateDate;
    @ApiModelProperty(value = "삭제 여부", example = "N", position = 10)
    private String delYn;

    @Builder
    public PostResponse(Long postIdx, String title, String contents, String thumbnailImg, String thumbnailContents, int boardCount, String privated, LocalDateTime createDate, LocalDateTime updateDate, String delYn) {
        this.postIdx = postIdx;
        this.title = title;
        this.contents = contents;
        this.thumbnailImg = thumbnailImg;
        this.thumbnailContents = thumbnailContents;
        this.boardCount = boardCount;
        this.privated = privated;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.delYn = delYn;
    }
}