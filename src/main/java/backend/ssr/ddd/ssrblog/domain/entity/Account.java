package backend.ssr.ddd.ssrblog.domain.entity;

import backend.ssr.ddd.ssrblog.oauth.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountIdx;

    private String profileImg;

    private String platform;

    private String email;

    private String name;

    private String accessToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String withdrawal = "N";

    @Builder
    public Account(Long accountIdx, String profileImg, String platform, String email, String name, String accessToken, Role role, String withdrawal) {
        this.accountIdx = accountIdx;
        this.profileImg = profileImg;
        this.platform = platform;
        this.email = email;
        this.name = name;
        this.accessToken = accessToken;
        this.role = role;
        this.withdrawal = withdrawal;
    }

    public Account update(String name, String profileImg) {
        this.name = name;
        this.profileImg = profileImg;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}
