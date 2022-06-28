package backend.ssr.ddd.ssrblog.friends.domain.entity;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.common.TimeEntity.RequestAcceptTimeEntity;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@IdClass(FriendsId.class)
public class Friends extends RequestAcceptTimeEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "requester_idx")
    private Account requesterIdx;

    @Id
    @ManyToOne
    @JoinColumn(name = "accepter_idx")
    private Account accepterIdx;

    private String accepted = "N";

    @Builder
    public Friends(Account requesterIdx, Account accepterIdx, String accepted) {
        this.requesterIdx = requesterIdx;
        this.accepterIdx = accepterIdx;
        this.accepted = accepted;
    }

    public void acceptFriend() {
        this.accepted = "Y";
    }

    public FriendsResponse toResponse() {
        FriendsResponse build = FriendsResponse.builder()
                .requesterIdx(requesterIdx.getAccountIdx())
                .accepterIdx(accepterIdx.getAccountIdx())
                .accepted(accepted)
                .requestDateTime(getRequestDateTime())
                .acceptedDateTime(getAcceptedDateTime())
                .build();

        return build;
    }
}
