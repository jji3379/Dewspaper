package backend.ssr.ddd.ssrblog.friends.domain.entity;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.common.TimeEntity.RequestAcceptTimeEntity;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsNoticeResponse;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsRequest;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "requester_notice_check_yn")
    private String requesterNoticeCheckYn = "N";

    @Column(name = "requester_notice_del_yn")
    private String requesterNoticeDelYn = "N";

    @Column(name = "accepter_notice_check_yn")
    private String accepterNoticeCheckYn = "N";

    @Column(name = "accepter_notice_del_yn")
    private String accepterNoticeDelYn = "N";

    @Builder
    public Friends(Account requesterIdx, Account accepterIdx, String accepted, String requesterNoticeCheckYn, String requesterNoticeDelYn, String accepterNoticeCheckYn, String accepterNoticeDelYn) {
        this.requesterIdx = requesterIdx;
        this.accepterIdx = accepterIdx;
        this.accepted = accepted;
        this.requesterNoticeCheckYn = requesterNoticeCheckYn;
        this.requesterNoticeDelYn = requesterNoticeDelYn;
        this.accepterNoticeCheckYn = accepterNoticeCheckYn;
        this.accepterNoticeDelYn = accepterNoticeDelYn;
    }

    public void acceptFriend() {
        this.accepted = "Y";
    }

    public void checkRequesterNotice() {
        this.requesterNoticeCheckYn = "Y";
    }

    public void checkAccepterNotice() {
        this.accepterNoticeCheckYn = "Y";
    }

    public void deleteRequesterNotice() {
        this.requesterNoticeCheckYn = "Y";
        this.requesterNoticeDelYn = "Y";
    }

    public void deleteAccepterNotice() {
        this.accepterNoticeCheckYn = "Y";
        this.accepterNoticeDelYn = "Y";
    }

    public FriendsResponse toResponse() {
        FriendsResponse build = FriendsResponse.builder()
                .requesterIdx(requesterIdx.getAccountIdx())
                .accepterIdx(accepterIdx.getAccountIdx())
                .accepted(accepted)
                .requesterNoticeCheckYn(requesterNoticeCheckYn)
                .requesterNoticeDelYn(requesterNoticeDelYn)
                .accepterNoticeCheckYn(accepterNoticeCheckYn)
                .accepterNoticeDelYn(accepterNoticeDelYn)
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
                .requesterNoticeCheckYn(requesterNoticeCheckYn)
                .requesterNoticeDelYn(requesterNoticeDelYn)
                .accepterNoticeCheckYn(accepterNoticeCheckYn)
                .accepterNoticeDelYn(accepterNoticeDelYn)
                .requestDateTime(getRequestDateTime())
                .acceptedDateTime(getAcceptedDateTime())
                .build();

        return build;
    }
}
