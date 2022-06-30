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
    Page<Post> findAllByPrivatedAndDeleted(Pageable pageable, String privated, String deleted);

    Optional<Post> findPostByPostIdxAndDeleted(Long postIdx, String deleted);

    Page<Post> findAllByPrivatedAndDeletedAndDateTimeBetween(Pageable pageable, String privated, String deleted, LocalDateTime startDateTime, LocalDateTime endDateTime);
}