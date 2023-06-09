package backend.ssr.ddd.ssrblog.account.domain.repository;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailAndPlatformAndWithdrawal(String email, String platform, String withdrawal);

    Optional<Account> findByAccountIdxAndWithdrawal(Long accountIdx, String withdrawal);
}
