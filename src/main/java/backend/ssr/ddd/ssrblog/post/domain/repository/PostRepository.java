package backend.ssr.ddd.ssrblog.post.domain.repository;

import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByPrivatedAndDelYn(Pageable pageable, String privated, String delYn);

    Optional<Post> findPostByPostIdxAndDelYn(Long postIdx, String delYn);

    Page<Post> findAllByPrivatedAndDelYnAndCreateDateBetween(Pageable pageable, String privated, String delYn, LocalDateTime startDateTime, LocalDateTime endDateTime);
}