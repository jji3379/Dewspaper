package backend.ssr.ddd.ssrblog.writer.domain.repository;

import backend.ssr.ddd.ssrblog.writer.domain.entity.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {
}
