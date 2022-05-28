package backend.ssr.ddd.ssrblog.domain.repository;

import backend.ssr.ddd.ssrblog.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
