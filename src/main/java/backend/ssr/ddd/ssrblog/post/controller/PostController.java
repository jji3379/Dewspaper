package backend.ssr.ddd.ssrblog.post.controller;

import backend.ssr.ddd.ssrblog.post.dto.PostRequest;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import backend.ssr.ddd.ssrblog.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "2. 게시물 API")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts/{term}") @ApiOperation(value = "게시물 전체 정보 조회", notes = "게시물 전체를 페이징 처리하여 조회한다. term 에 all(전체), weekly(1주일), monthly(한 달) 을 입력하여 기간별 게시물을 조회한다. (공개 되어있으면서 (privated = N), 삭제 되지 않은 게시물들만 조회 (deleted = N))")
    @ApiImplicitParam(name = "term", required = true, value = "예 : weekly")
    public ResponseEntity<Page<PostResponse>> getPostPagingList(Pageable pageable, @PathVariable String term) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());

        Page<PostResponse> postList = postService.getPostList(pageable, term);

        return new ResponseEntity(postList, HttpStatus.OK);
    }

    @GetMapping("/post/{postIdx}") @ApiOperation(value = "게시물 조회", notes = "게시물 번호가 postIdx 인 게시물의 정보를 조회한다.")
    @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postIdx) {
        PostResponse post = postService.getPost(postIdx);

        return new ResponseEntity(post, HttpStatus.OK);
    }

    @PostMapping("/post") @ApiOperation(value = "게시물 등록", notes = "postRequest 를 받아 새로운 게시물을 등록한다. (함께 작성한 coWirter 의 accountIdx 에 realWriter 의 accountIdx 를 포함시켜주세요.)")
    public ResponseEntity<PostResponse> savePost(@RequestBody @Valid PostRequest postRequest, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(errors.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
        }

        PostResponse postResponse = postService.savePost(postRequest);

        return new ResponseEntity(postResponse, HttpStatus.CREATED);
    }

    @PutMapping("/post/{postIdx}") @ApiOperation(value = "게시물 정보 수정", notes = "게시물 번호가 postIdx 인 게시물을 postRequest 를 받아 게시물의 정보를 수정한다. (함께 작성한 coWirter 의 accountIdx 에 realWriter 의 accountIdx 를 포함시켜주세요.)")
    @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postIdx, @RequestBody @Valid PostRequest postRequest, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(errors.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
        }

        PostResponse post = postService.updatePost(postIdx, postRequest);
        return new ResponseEntity(post, HttpStatus.OK);
    }

    @DeleteMapping("/post/{postIdx}") @ApiOperation(value = "게시물 삭제", notes = "게시물 번호가 postIdx 인 게시물을 삭제한다. (게시물 삭제시 함께 작성한 작성자와 댓글도 함께 삭제된다.)")
    @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1")
    public void deletePost(@PathVariable Long postIdx) {

        postService.deletePost(postIdx);
    }
}
