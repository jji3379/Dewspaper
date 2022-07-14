package backend.ssr.ddd.ssrblog.friends.domain.entity;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.common.TimeEntity.RequestAcceptTimeEntity;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsNoticeResponse;
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

    @Column(name = "accepted")
    private String accepted = "W";

    @Column(name = "notice_check_yn")
    private String noticeCheckYn = "N";

    @Column(name = "notice_del_yn")
    private String noticeDelYn = "N";

    @Builder
    public Friends(Account requesterIdx, Account accepterIdx, String accepted, String noticeCheckYn, String noticeDelYn) {
        this.requesterIdx = requesterIdx;
        this.accepterIdx = accepterIdx;
        this.accepted = accepted;
        this.noticeCheckYn = noticeCheckYn;
        this.noticeDelYn = noticeDelYn;
    }

    public void acceptFriend() {
        this.accepted = "Y";
    }

    public FriendsResponse toResponse() {
        FriendsResponse build = FriendsResponse.builder()
                .requesterIdx(requesterIdx.getAccountIdx())
                .accepterIdx(accepterIdx.getAccountIdx())
                .accepted(accepted)
                .noticeCheckYn(noticeCheckYn)
                .noticeDelYn(noticeDelYn)
                .requestDateTime(getRequestDateTime())
                .acceptedDateTime(getAcceptedDateTime())
                .build();

        return build;
    }

    public FriendsNoticeResponse toNoticeResponse() {
        FriendsNoticeResponse build = FriendsNoticeResponse.builder()
                .requesterIdx(requesterIdx.toFriendsResponse())
                .accepterIdx(accepterIdx.toFriendsResponse())
                .accepted(accepted)
                .noticeCheckYn(noticeCheckYn)
                .noticeDelYn(noticeDelYn)
                .requestDateTime(getRequestDateTime())
                .acceptedDateTime(getAcceptedDateTime())
                .build();

        return build;
    }
}
