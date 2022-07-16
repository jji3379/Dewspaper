package backend.ssr.ddd.ssrblog.friends.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.entity.QAccount;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import backend.ssr.ddd.ssrblog.friends.domain.entity.QFriends;
import backend.ssr.ddd.ssrblog.friends.domain.repository.FriendsRepository;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsNoticeResponse;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsRequest;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendsRepository friendsRepository;

    private final AccountRepository accountRepository;

    private final JPAQueryFactory jpaQueryFactory;

    // 요청 받은 사람의 입장이기 때문에 accepterIdx 를 파라미터로 받는다.

    /**
     * 친구 알림 목록
     */
    public Page<FriendsNoticeResponse> getNoticeList(Pageable pageable, Account accountIdx) {
        Page<Friends> getNoticeList = friendsRepository.findByRequesterIdxAndRequesterNoticeDelYnOrAccepterIdxAndAccepterNoticeDelYnOrderByRequestDateTimeDesc(pageable, accountIdx,"N", accountIdx, "N");

        List<FriendsNoticeResponse> friendsResponseList = new ArrayList<>();
        for (Friends friends : getNoticeList) {
            friendsResponseList.add(friends.toNoticeResponse());
        }

        return new PageImpl<>(friendsResponseList, pageable, getNoticeList.getTotalElements());
    }

    /**
     * 친구 목록 리스트
     */
    @Transactional(readOnly = true)
    public List<Account> getFriendsList(Account accountIdx) {
        QFriends qFriends = QFriends.friends;
        QAccount qAccount = QAccount.account;

        List<Account> requesterFriendsList = jpaQueryFactory.select(qAccount).distinct()
                .from(qAccount)
                .where(qAccount.accountIdx.in(
                                JPAExpressions.select(qFriends.accepterIdx.accountIdx.as("accountIdx")) // 내가 요청을 보내서 친구가 된 경우
                                        .from(qFriends)
                                        .where(qFriends.requesterIdx.eq(accountIdx)
                                                .and(qFriends.accepted.eq("Y"))
                                        )
                        ).or(qAccount.accountIdx.in(
                                JPAExpressions.select(qFriends.requesterIdx.accountIdx.as("accountIdx"))
                                        .from(qFriends)
                                        .where(qFriends.accepterIdx.eq(accountIdx)
                                                        .and(qFriends.accepted.eq("Y"))
                                        ))
                ))
                .fetch();

        return requesterFriendsList;
    }

    /**
     * 내가 보낸 친구 요청
     */
    @Transactional(readOnly = true)
    public List<Friends> getRequireToFriendsList(Account requesterIdx) {

        return friendsRepository.findByRequesterIdx(requesterIdx);
    }

    /**
     * 친구 요청
     */
    @Transactional
    public Friends newFriendRequest(FriendsRequest friendsRequest) {
        // 요청자가 회원인지 확인
        accountRepository.findById(friendsRequest.getRequesterIdx().getAccountIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REQUESTER));

        // 수락자가 회원인지 확인
        accountRepository.findById(friendsRequest.getAccepterIdx().getAccountIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCEPTER));

        // 이미 요청을 보낸 경우
        Optional<Friends> existRequest = friendsRepository.findByRequesterIdxAndAccepterIdx(friendsRequest.getRequesterIdx(), friendsRequest.getAccepterIdx());
        // 상대방이 요청을 보낸 경우
        Optional<Friends> existAcceptRequest = friendsRepository.findByRequesterIdxAndAccepterIdx(friendsRequest.getAccepterIdx(), friendsRequest.getRequesterIdx());

        // 이미 같은 요청을 보냈을 경우
        if (existRequest.isPresent() || existAcceptRequest.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_FRIEND_REQUEST);
        } else {
            return friendsRepository.save(friendsRequest.toEntity());
        }
    }

    /**
     * 친구 수락
     */
    @Transactional
    public Friends acceptFriend(FriendsRequest friendsRequest) {
        Friends getRequesterRequest = friendsRepository.findByRequesterIdxAndAccepterIdx(friendsRequest.getRequesterIdx(), friendsRequest.getAccepterIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRIEND_REQUEST));

        getRequesterRequest.acceptFriend();

        return friendsRepository.save(getRequesterRequest);
    }

    /**
     * 알림 확인
     * 그 사람의 accountIdx 를 받아서
     * 그 사람이 requester 에 있는지
     * accepter 에 있는지 확인하고
     * 각각의 경우로 뽑아서 check 하기
     */
    @Transactional
    public List<FriendsNoticeResponse> checkNotice(Account accountIdx) {
        List<Friends> requesterNoticeCheckYn = friendsRepository.findByRequesterIdxAndRequesterNoticeCheckYn(accountIdx, "N");
        List<Friends> accepterNoticeCheckYn = friendsRepository.findByAccepterIdxAndAccepterNoticeCheckYn(accountIdx, "N");

        /**
         * 추후 벌크 연산으로 수정
         * https://dev-gorany.tistory.com/327
         */

        // requester 일 경우
        for (Friends requester: requesterNoticeCheckYn) {
            requester.checkRequesterNotice();
            friendsRepository.save(requester);
        }
        // accepter 일 경우
        for (Friends accepter: accepterNoticeCheckYn) {
            accepter.checkAccepterNotice();
            friendsRepository.save(accepter);
        }

        List<Friends> responseList = friendsRepository.findByRequesterIdxAndRequesterNoticeDelYnOrAccepterIdxAndAccepterNoticeDelYnOrderByRequestDateTimeDesc(accountIdx,"N", accountIdx, "N");
        List<FriendsNoticeResponse> noticeResponseList = new ArrayList<>();
        for (Friends friends : responseList) {
            noticeResponseList.add(friends.toNoticeResponse());
        }

        return noticeResponseList;
    }

    /**
     * 알림 삭제
     */
    @Transactional
    public void deleteNotice(Account accountIdx, Account requesterIdx, Account accepterIdx) {

        if (accountIdx.getAccountIdx().equals(requesterIdx.getAccountIdx())) { // 사용자가 요청자일 경우
            Friends requester = friendsRepository.findByRequesterIdxAndAccepterIdxAndRequesterNoticeDelYn(requesterIdx, accepterIdx, "N")
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ALARM));
            requester.deleteRequesterNotice();
            friendsRepository.save(requester);

        } else if (accountIdx.getAccountIdx().equals(accepterIdx.getAccountIdx())) { // 사용자가 수락자일 경우
            Friends accepter = friendsRepository.findByRequesterIdxAndAccepterIdxAndAccepterNoticeDelYn(requesterIdx, accepterIdx, "N")
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ALARM));
            accepter.deleteAccepterNotice();
            friendsRepository.save(accepter);
        }
    }

    /**
     * 친구 삭제
     */
    @Transactional
    public void deleteFriend(Account requesterIdx, Account accepterIdx) {
        Optional<Friends> requesterFriend = friendsRepository.findByRequesterIdxAndAccepterIdx(requesterIdx, accepterIdx);
        Optional<Friends> accepterFriend = friendsRepository.findByRequesterIdxAndAccepterIdx(accepterIdx, requesterIdx);

        // 둘 다 요청이 없었을 경우
        if (!requesterFriend.isPresent() && !accepterFriend.isPresent()) {
            throw  new CustomException(ErrorCode.NOT_FOUND_FRIEND_REQUEST);
        } else if (requesterFriend.isPresent()) {
            friendsRepository.delete(requesterFriend.get());
        } else if (accepterFriend.isPresent()) {
            friendsRepository.delete(accepterFriend.get());
        }

    }

    @Transactional(readOnly = true)
    public long getAccountFriendsCount(Account accountIdx) {

        return friendsRepository.countByRequesterIdxOrAccepterIdx(accountIdx, accountIdx);
    }
}
