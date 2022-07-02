package backend.ssr.ddd.ssrblog.comment.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.comment.domain.entity.Comment;
import backend.ssr.ddd.ssrblog.comment.domain.repository.CommentRepository;
import backend.ssr.ddd.ssrblog.comment.dto.CommentRequest;
import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final AccountRepository accountRepository;

    public Page<Comment> getCommentList(Pageable pageable, Post postIdx) {

        return commentRepository.findAllByPostIdx(pageable, postIdx);
    }

    public Comment saveComment(Long postIdx, CommentRequest commentRequest) {
        postRepository.findById(postIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        Account account = accountRepository.findById(commentRequest.getAccountIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));

        Comment comment = commentRequest.toEntity(postIdx, account);

        return commentRepository.save(comment);
    }

    public Comment updateComment(Post postIdx, Long commentIdx, CommentRequest commentRequest) {
        Comment comment = commentRepository.findByPostIdxAndCommentIdx(postIdx, commentIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMNET));

        if (comment.getAccountIdx().getAccountIdx() != commentRequest.getAccountIdx()) {
            throw new CustomException(ErrorCode.INVALID_WRITER);
        }

        comment.update(commentRequest);

        return commentRepository.save(comment);
    }

    public void deleteComment(Post postIdx, Long commentIdx) {
        Comment comment = commentRepository.findByPostIdxAndCommentIdx(postIdx, commentIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMNET));

        commentRepository.delete(comment);
    }
}
