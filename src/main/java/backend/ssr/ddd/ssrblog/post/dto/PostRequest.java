package backend.ssr.ddd.ssrblog.post.dto;

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
    private String thumnailImg;

    @ApiModelProperty(value = "썸네일 내용", example = "SSR은 서버에서 ~~~", position = 4)
    private String thumnailContents;


    public Post toEntity() {
        Post build = Post.builder()
                .title(title)
                .contents(contents)
                .thumnailImg(thumnailImg)
                .thumnailContents(thumnailContents)
                .board_count(0)
                .privated("N")
                .deleted("N")
                .build();

        return build;
    }
}
