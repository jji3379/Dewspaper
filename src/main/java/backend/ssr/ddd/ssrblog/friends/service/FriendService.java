package backend.ssr.ddd.ssrblog.friends.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.entity.QAccount;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import backend.ssr.ddd.ssrblog.friends.domain.entity.QFriends;
import backend.ssr.ddd.ssrblog.friends.domain.repository.FriendsRepository;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsRequest;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
     * 친구 요청 받은 목록 리스트
     */
    public List<Friends> getRequiredFriendsList(Account accepterIdx) {

        return friendsRepository.findByAccepterIdx(accepterIdx);
    }

    /**
     * 친구 목록 리스트
     */
    public List<Account> getFriendsList(Account accountIdx) {
        QFriends qFriends = QFriends.friends;
        QAccount qAccount = QAccount.account;

        // account 에서 friends 에서 8 말고 값들 뽑기

        // friends 에서 목록들 뽑아서


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
    public List<Friends> getRequireToFriendsList(Account requesterIdx) {

        return friendsRepository.findByRequesterIdx(requesterIdx);
    }

    /**
     * 친구 요청
     */
    public Friends newFriendRequest(FriendsRequest friendsRequest) {
        // 요청자가 회원인지 확인
        accountRepository.findById(friendsRequest.getRequesterIdx().getAccountIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REQUESTER));

        // 수락자가 회원인지 확인
        accountRepository.findById(friendsRequest.getAccepterIdx().getAccountIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCEPTER));

        /**
         * QueryDSL
         */
        QFriends qFriends = QFriends.friends;

        // 이미 상대방이 요청을 보낸 후 수락하여 친구가 된 경우 요청을 받을 수 없게 한다.
        Optional<List<Friends>> isFriend = Optional.ofNullable(jpaQueryFactory.selectFrom(qFriends)
                .where(qFriends.requesterIdx.eq(friendsRequest.getAccepterIdx())
                        .and(qFriends.accepterIdx.eq(friendsRequest.getRequesterIdx()))
                        .and(qFriends.accepted.eq("Y"))
                        .or(qFriends.requesterIdx.eq(friendsRequest.getRequesterIdx())
                                .and(qFriends.accepterIdx.eq(friendsRequest.getAccepterIdx()))
                                .and(qFriends.accepted.eq("Y")))
                )
                .fetch());

        // 이미 수락된 요청이 있을 경우
        if (!isFriend.get().isEmpty()) {
            throw new CustomException(ErrorCode.DUPLICATE_FRIEND);
        }

        Optional<Friends> existRequest = friendsRepository.findByRequesterIdxAndAccepterIdx(friendsRequest.getRequesterIdx(), friendsRequest.getAccepterIdx());

        // 이미 같은 요청을 보냈을 경우
        if (existRequest.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_FRIEND_REQUEST);
        } else {
            return friendsRepository.save(friendsRequest.toEntity());
        }
    }

    /**
     * 친구 수락
     */
    public Friends acceptFriend(FriendsRequest friendsRequest) {
        Friends getRequesterRequest = friendsRepository.findByRequesterIdxAndAccepterIdx(friendsRequest.getRequesterIdx(), friendsRequest.getAccepterIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRIEND_REQUEST));

        if (getRequesterRequest != null) {
            // 양쪽 다 요청을 보냈을 경우 (없어도 이상한 경우가 아니기에 에러를 던지지 않음)
            Optional<Friends> getAccepterRequest = friendsRepository.findByRequesterIdxAndAccepterIdx(friendsRequest.getAccepterIdx(), friendsRequest.getRequesterIdx());

            // 반대쪽 요청도 수락
            if (getAccepterRequest.isPresent()) {
                Friends accepterRequest = getAccepterRequest.get();
                accepterRequest.acceptFriend();

                friendsRepository.save(accepterRequest);
            }
        }

        getRequesterRequest.acceptFriend();

        return friendsRepository.save(getRequesterRequest);
    }

    /**
     * 친구 삭제
     */
    public void deleteFriend(FriendsRequest friendsRequest) {
        Friends friends = friendsRepository.findByRequesterIdxAndAccepterIdx(friendsRequest.getRequesterIdx(), friendsRequest.getAccepterIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRIEND_REQUEST));

        friendsRepository.delete(friends);
    }
}
