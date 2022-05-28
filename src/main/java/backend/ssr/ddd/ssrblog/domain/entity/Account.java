package backend.ssr.ddd.ssrblog.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private Long accountIdx;

    private String profileImg;

    private String platform;

    private String email;

    private String name;

    private String accessToken;

    private String withdrawal = "N";
}
