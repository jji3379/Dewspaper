package backend.ssr.ddd.ssrblog.friends.dto;


import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import lombok.Builder;

public class FriendsRequest {
    private Long requesterIdx;
    private Long accepterIdx;

    @Builder
    public FriendsRequest(Long requesterIdx, Long accepterIdx) {
        this.requesterIdx = requesterIdx;
        this.accepterIdx = accepterIdx;
    }

    public Friends toEntity() {
        Friends build = Friends.builder()
                .requesterIdx(requesterIdx)
                .accepterIdx(accepterIdx)
                .accepted("N")
                .build();

        return build;
    }
}
