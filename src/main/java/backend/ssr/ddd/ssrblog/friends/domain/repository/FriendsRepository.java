package backend.ssr.ddd.ssrblog.friends.domain.repository;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Account> {

    List<Friends> findByAccepterIdx(Account accepterIdx);
    Optional<Friends> findByRequesterIdxAndAccepterIdx(Account requesterIdx, Account accepterIdx);
    List<Friends> findByRequesterIdx(Account accepterIdx);
}
