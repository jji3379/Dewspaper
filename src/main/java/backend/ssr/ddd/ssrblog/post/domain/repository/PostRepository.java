package backend.ssr.ddd.ssrblog.post.domain.repository;

import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}