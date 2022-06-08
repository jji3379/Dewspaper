package backend.ssr.ddd.ssrblog.post.domain.repository;

import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByPrivatedAndDeleted(Pageable pageable, String privated, String deleted);

    Optional<Post> findPostByPostIdxAndDeleted(Long postIdx, String deleted);
}