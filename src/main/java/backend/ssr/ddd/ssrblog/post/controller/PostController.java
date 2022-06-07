package backend.ssr.ddd.ssrblog.post.controller;

import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.post.dto.PostRequest;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import backend.ssr.ddd.ssrblog.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public List<PostResponse> getPostPagingList(Pageable pageable) {
        List<Post> postList = postService.getPostList();
        List<PostResponse> list = new ArrayList();

        for (Post post : postList) {
            PostResponse response = new PostResponse();
            list.add(post.toResponse());
        }

        return list;
    }

    @GetMapping("/post/{postIdx}")
    public PostResponse getPost(@PathVariable Long postIdx) {
        Post post = postService.getPost(postIdx);

        return post.toResponse();
    }

    @PostMapping("/post")
    public PostResponse savePost(@RequestBody PostRequest postRequest) {
        Post post = postService.savePost(postRequest);

        return post.toResponse();
    }

    @PutMapping("/post/{postIdx}")
    public PostResponse updatePost(@PathVariable Long postIdx, @RequestBody PostRequest postRequest) {
        Post post = postService.updatePost(postIdx, postRequest);

        return post.toResponse();
    }

    @DeleteMapping("/post/{postIdx}")
    public void deletePost(@PathVariable Long postIdx) {

        postService.deletePost(postIdx);
    }
}
