package backend.ssr.ddd.ssrblog.comment.domain.repository;

import backend.ssr.ddd.ssrblog.comment.domain.entity.Comment;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByPostIdxAndDelYn(Pageable pageable, Post postIdx, String delYn);

    Optional<Comment> findByPostIdxAndCommentIdxAndDelYn(Post postIdx, Long commentIdx, String delYn);
}
