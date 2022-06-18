package backend.ssr.ddd.ssrblog.friends.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Friends {

    @Id
    private Long friendsIdx;

    private Long requesterIdx;

    private Long accepterIdx;

    private String accepted = "N";

    @CreatedDate
    private LocalDateTime requestDateTime;

    @LastModifiedDate
    private LocalDateTime acceptedDateTime;

    @Builder
    public Friends(Long friendsIdx, Long requesterIdx, Long accepterIdx, String accepted, LocalDateTime requestDateTime, LocalDateTime acceptedDateTime) {
        this.friendsIdx = friendsIdx;
        this.requesterIdx = requesterIdx;
        this.accepterIdx = accepterIdx;
        this.accepted = accepted;
        this.requestDateTime = requestDateTime;
        this.acceptedDateTime = acceptedDateTime;
    }

    public void acceptFriend() {
        this.accepted = "Y";
    }

    public void deleteFriend() {
        this.accepted = "D";
    }
}
