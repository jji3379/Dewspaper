package backend.ssr.ddd.ssrblog.writer.domain.repository;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.writer.domain.entity.Writer;
import backend.ssr.ddd.ssrblog.writer.domain.entity.WriterId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WriterRepository extends JpaRepository<Writer, WriterId> {
    Optional<Writer> findByPostIdxAndAccountIdxAndDelYn(Post postIdx, Account accountIdx, String delYn);

    List<Writer> findByPostIdxAndDelYn(Post postIdx, String delYn);

    Page<Writer> findByAccountIdxAndRealWriterAndDelYnOrderByCreateDateDesc(Pageable pageable, Account accountIdx, Account realWriter, String delYn);

    long countByAccountIdx(Account accountIdx);
}
