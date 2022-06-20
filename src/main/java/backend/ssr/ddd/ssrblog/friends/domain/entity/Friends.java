package backend.ssr.ddd.ssrblog.friends.domain.entity;

import backend.ssr.ddd.ssrblog.common.TimeEntity.RequestAcceptTimeEntity;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@NoArgsConstructor
@Getter
@IdClass(FriendsId.class)
public class Friends extends RequestAcceptTimeEntity {

    @Id
    private Long requesterIdx;

    @Id
    private Long accepterIdx;

    private String accepted = "N";

    @Builder
    public Friends(Long requesterIdx, Long accepterIdx, String accepted) {
        this.requesterIdx = requesterIdx;
        this.accepterIdx = accepterIdx;
        this.accepted = accepted;
    }

    public void acceptFriend() {
        this.accepted = "Y";
    }

    public FriendsResponse toResponse() {
        FriendsResponse build = FriendsResponse.builder()
                .requesterIdx(requesterIdx)
                .accepterIdx(accepterIdx)
                .accepted(accepted)
                .requestDateTime(getRequestDateTime())
                .acceptedDateTime(getAcceptedDateTime())
                .build();

        return build;
    }
}
