package backend.ssr.ddd.ssrblog.friends.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FriendsId implements Serializable {
    private Long requesterIdx;
    private Long accepterIdx;
}
