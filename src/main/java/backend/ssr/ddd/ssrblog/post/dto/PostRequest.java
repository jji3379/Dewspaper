package backend.ssr.ddd.ssrblog.post.dto;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.writer.domain.entity.Writer;
import backend.ssr.ddd.ssrblog.writer.dto.WriterRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "게시물 요청 정보")
public class PostRequest {
    @NotBlank(message = "게시물의 제목은 필수 입력 항목입니다.")
    @Size(max = 100, message = "게시물 제목은 최대 100자까지 입력 가능합니다.")
    @ApiModelProperty(value = "게시물 제목", required = true, example = "server-side rendering", position = 1)
    private String title;

    @NotBlank(message = "게시물 내용은 필수 입력 항목입니다.")
    @ApiModelProperty(value = "게시물 내용", required = true, example = "SSR은 서버에서 사용자에게 보여줄 페이지를 모두 구성하여 사용자에게 페이지를 보여주는 방식이다. JSP/Servlet의 아키텍처에서 이 방식을 사용했다.", position = 2)
    private String contents;

    @ApiModelProperty(value = "썸네일 이미지", example = "http://upload2.inven.co.kr/upload/2019/12/27/bbs/i14210693079.jpg", position = 3)
    private String thumbnailImg;

    @ApiModelProperty(value = "썸네일 내용", example = "SSR은 서버에서 ~~~", position = 4)
    private String thumbnailContents;

    @ApiModelProperty(value = "함께한 동료", example = "", position = 4)
    private List<WriterRequest> cowriter;


    public Post toEntity() {
        Post build = Post.builder()
                .title(title)
                .contents(contents)
                .thumbnailImg(thumbnailImg)
                .thumbnailContents(thumbnailContents)
                .boardCount(0)
                .privated("N")
                .delYn("N")
                .build();

        return build;
    }
}
