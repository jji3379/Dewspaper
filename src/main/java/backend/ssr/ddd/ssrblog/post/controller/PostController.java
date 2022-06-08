package backend.ssr.ddd.ssrblog.post.controller;

import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.post.dto.PostRequest;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import backend.ssr.ddd.ssrblog.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "2. 게시물 API")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts") @ApiOperation(value = "게시물 전체 정보 조회", notes = "게시물 전체를 페이징 처리하여 조회한다. (공개 되어있으면서 (privated = N), 삭제 되지 않은 게시물들만 조회 (deleted = N))")
    public Page<PostResponse> getPostPagingList(Pageable pageable) {
        List<Post> postList = postService.getPostList(pageable);
        List<PostResponse> list = new ArrayList();

        for (Post post : postList) {
            PostResponse response = new PostResponse();
            list.add(post.toResponse());
        }


        return new PageImpl<>(list, pageable, list.size());
    }

    @GetMapping("/post/{postIdx}") @ApiOperation(value = "게시물 조회", notes = "게시물 번호가 postIdx 인 게시물의 정보를 조회한다.")
    @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1")
    public PostResponse getPost(@PathVariable Long postIdx) {
        Post post = postService.getPost(postIdx);

        return post.toResponse();
    }

    @PostMapping("/post") @ApiOperation(value = "게시물 등록", notes = "postRequest 를 받아 새로운 게시물을 등록한다.")
    public PostResponse savePost(@RequestBody @Valid PostRequest postRequest, Errors errors) {
        Post post = postService.savePost(postRequest);

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldError().getDefaultMessage());
        }

        return post.toResponse();
    }

    @PutMapping("/post/{postIdx}") @ApiOperation(value = "게시물 정보 수정", notes = "게시물 번호가 postIdx 인 게시물을 postRequest 를 받아 게시물의 정보를 수정한다.")
    @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1")
    public PostResponse updatePost(@PathVariable Long postIdx, @RequestBody @Valid PostRequest postRequest, Errors errors) {
        Post post = postService.updatePost(postIdx, postRequest);

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldError().getDefaultMessage());
        }

        return post.toResponse();
    }

    @DeleteMapping("/post/{postIdx}") @ApiOperation(value = "게시물 삭제", notes = "게시물 번호가 postIdx 인 게시물을 삭제한다.")
    @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1")
    public void deletePost(@PathVariable Long postIdx) {

        postService.deletePost(postIdx);
    }
}
