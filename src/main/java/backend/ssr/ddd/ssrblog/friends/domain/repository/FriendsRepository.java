package backend.ssr.ddd.ssrblog.friends.domain.repository;

import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {

    List<Friends> findByAccepterIdxAndAccepted(Long accepterIdx, String accepted);
    Optional<Friends> findByRequesterIdxAndAccepterIdx(Long requesterIdx, Long accepterIdx);
    List<Friends> findByRequesterIdx(Long accepterIdx);
}
