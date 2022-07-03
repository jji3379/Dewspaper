package backend.ssr.ddd.ssrblog.comment.controller;

import backend.ssr.ddd.ssrblog.comment.domain.entity.Comment;
import backend.ssr.ddd.ssrblog.comment.dto.CommentRequest;
import backend.ssr.ddd.ssrblog.comment.dto.CommentResponse;
import backend.ssr.ddd.ssrblog.comment.service.CommentService;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "4. 댓글 API")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postIdx}/comments") @ApiOperation(value = "게시물에 해당하는 댓글 조회", notes = "postIdx 에 해당하는 댓글 전체를 페이징 처리하여 조회한다.")
    @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1")
    public ResponseEntity<Page<CommentResponse>> getCommentsList(Pageable pageable, @PathVariable Post postIdx) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());

        Page<Comment> commentList = commentService.getCommentList(pageable, postIdx);

        List<CommentResponse> responseList = new ArrayList<>();
        for (Comment comment : commentList) {
            responseList.add(comment.toResponse());
        }

        return new ResponseEntity(new PageImpl<>(responseList, pageable, commentList.getTotalElements()), HttpStatus.OK);
    }

    @PostMapping("/post/{postIdx}/comment") @ApiOperation(value = "댓글 등록", notes = "게시물 번호가 postIdx 인 게시물에 commentRequest 를 받아 댓글을 등록한다.")
    @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1")
    public ResponseEntity<CommentResponse> saveComment(@PathVariable Long postIdx, @RequestBody @Valid CommentRequest commentRequest, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        Comment comment = commentService.saveComment(postIdx, commentRequest);

        return new ResponseEntity<>(comment.toResponse(), HttpStatus.CREATED);
    }

    @PutMapping("/post/{postIdx}/comment/{commentIdx}") @ApiOperation(value = "댓글 정보 수정", notes = "게시물 번호가 postIdx 이고 댓글의 번호가 commentIdx 인 댓글의 정보를 commentRequest 를 받아 수정한다.")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1"),
        @ApiImplicitParam(name = "commentIdx", required = true, value = "예 : 1")
    })
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Post postIdx, @PathVariable Long commentIdx, @RequestBody @Valid CommentRequest commentRequest, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        Comment comment = commentService.updateComment(postIdx, commentIdx, commentRequest);

        return new ResponseEntity<>(comment.toResponse(), HttpStatus.OK);
    }

    @DeleteMapping("/post/{postIdx}/comment/{commentIdx}") @ApiOperation(value = "댓글 정보 삭제", notes = "게시물 번호가 postIdx 이고 댓글의 번호가 commentIdx 인 댓글을 삭제한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postIdx", required = true, value = "예 : 1"),
            @ApiImplicitParam(name = "commentIdx", required = true, value = "예 : 1")
    })
    public void deleteComment(@PathVariable Post postIdx, @PathVariable Long commentIdx) {

        commentService.deleteComment(postIdx, commentIdx);
    }

}
