package backend.ssr.ddd.ssrblog.post.dto;

import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private String thumnail_img;

    private String thumnail_contents;

    private String title;

    private String contents;

    public Post toEntity() {
        Post build = Post.builder()
                .thumnail_img(thumnail_img)
                .thumnail_contents(thumnail_contents)
                .title(title)
                .contents(contents)
                .board_count(0)
                .privated("N")
                .deleted("N")
                .build();

        return build;
    }
}
