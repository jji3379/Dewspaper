package backend.ssr.ddd.ssrblog.oauth;

import backend.ssr.ddd.ssrblog.domain.entity.Account;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionAccount implements Serializable {
    private String name;
    private String email;
    private String profileImg;

    public SessionAccount(Account account) {
        this.name = account.getName();
        this.email = account.getEmail();
        this.profileImg = account.getProfileImg();
    }
}
