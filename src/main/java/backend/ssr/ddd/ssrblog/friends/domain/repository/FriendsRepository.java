package backend.ssr.ddd.ssrblog.friends.domain.repository;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import backend.ssr.ddd.ssrblog.friends.domain.entity.FriendsId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, FriendsId> {

    Optional<Friends> findByRequesterIdxAndAccepterIdx(Account requesterIdx, Account accepterIdx);
    List<Friends> findByRequesterIdxOrAccepterIdx(Account requesterIdx, Account accepterIdx);
    List<Friends> findByRequesterIdx(Account accepterIdx);
    Page<Friends> findByRequesterIdxAndRequesterNoticeDelYnOrAccepterIdxAndAccepterNoticeDelYnOrderByRequestDateTimeDesc(Pageable pageable, Account requesterIdx, String requesterDelYn, Account accepterIdx, String accepterDelYn);
    List<Friends> findByRequesterIdxAndRequesterNoticeDelYnOrAccepterIdxAndAccepterNoticeDelYnOrderByRequestDateTimeDesc(Account requesterIdx, String requesterDelYn, Account accepterIdx, String accepterDelYn);
    long countByRequesterIdxOrAccepterIdx(Account requsterIdx, Account accepterIdx);

    // 요청자 알림 확인 여부
    List<Friends> findByRequesterIdxAndRequesterNoticeCheckYn(Account requesterIdx, String requesterNoticeCheckYn);
    // 수락자 알림 확인 여부
    List<Friends> findByAccepterIdxAndAccepterNoticeCheckYn(Account accepterIdx, String accepterNoticeCheckYn);

    // 요청자 알림 삭제 여부
    Optional<Friends> findByRequesterIdxAndAccepterIdxAndRequesterNoticeDelYn(Account requesterIdx, Account accepterIdx, String requesterNoticeDelYn);
    // 수락자 알림 삭제 여부
    Optional<Friends> findByRequesterIdxAndAccepterIdxAndAccepterNoticeDelYn(Account requesterIdx, Account accepterIdx, String accepterNoticeDelYn);
    // 새로운 알림 여부
    long countByRequesterIdxAndRequesterNoticeCheckYnOrAccepterIdxAndAccepterNoticeCheckYn(Account requsterIdx, String requesterCheckYn, Account accepterIdx, String accepterCheckYn);
}
