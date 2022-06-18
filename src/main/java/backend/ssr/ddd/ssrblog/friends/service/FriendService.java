package backend.ssr.ddd.ssrblog.friends.service;

import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import backend.ssr.ddd.ssrblog.friends.domain.repository.FriendsRepository;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendsRepository friendsRepository;

    // 요청 받은 사람의 입장이기 때문에 accepterIdx 를 파라미터로 받는다.

    /**
     * 친구 요청 받은 목록 리스트
     */
    public List<Friends> getRequiredFriendsList(Long accepterIdx) {

        return friendsRepository.findByAccepterIdxAndAccepted(accepterIdx, "N");
    }

    /**
     * 내가 보낸 친구 요청
     */
    public List<Friends> getRequireToFriendsList(Long requesterIdx) {

        return friendsRepository.findByRequesterIdx(requesterIdx);
    }

    /**
     * 친구 요청
     */
    public Friends saveFriends(FriendsRequest friendsRequest) {

        return friendsRepository.save(friendsRequest.toEntity());
    }

    /**
     * 친구 수락
     */
    public void acceptFriend(Long friendsIdx) {
        Friends friends = friendsRepository.findById(friendsIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRIENDS));

        friends.acceptFriend();
    }

    /**
     * 친구 삭제 - 차단
     */
    public void deleteFriend(Long friendsIdx) {
        Friends friends = friendsRepository.findById(friendsIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRIENDS));

        friends.acceptFriend();
    }
}
