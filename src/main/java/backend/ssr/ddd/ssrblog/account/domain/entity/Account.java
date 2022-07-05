package backend.ssr.ddd.ssrblog.account.domain.entity;

import backend.ssr.ddd.ssrblog.account.dto.AccountMeResponse;
import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.account.dto.Role;
import backend.ssr.ddd.ssrblog.common.TimeEntity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
public class Account extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_idx")
    private Long accountIdx;

    private String platform;

    private String email;

    private String name;

    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String emailAg;

    private String alarmAg;

    private String withdrawal = "N";

    public AccountResponse toResponse() {
        AccountResponse build = AccountResponse.builder()
                .accountIdx(accountIdx)
                .platform(platform)
                .email(email)
                .name(name)
                .profileImg(profileImg)
                .role(role.getRole())
                .emailAg(emailAg)
                .alarmAg(alarmAg)
                .createDate(getCreateDate())
                .updateDate(getUpdateDate())
                .withdrawal(withdrawal)
                .build();
        return build;
    }

    @Builder
    public Account(Long accountIdx, String platform, String email, String name, String profileImg, Role role, String emailAg, String alarmAg, String withdrawal) {
        this.accountIdx = accountIdx;
        this.platform = platform;
        this.email = email;
        this.name = name;
        this.profileImg = profileImg;
        this.role = role;
        this.emailAg = emailAg;
        this.alarmAg = alarmAg;
        this.withdrawal = withdrawal;
    }

    public AccountMeResponse toResponseMe() {
        AccountMeResponse build = AccountMeResponse.builder()
                .name(name)
                .profileImg(profileImg)
                .build();
        return build;
    }

    public Account update(String platform, String name) {
        this.platform = platform;
        this.name = name;

        return this;
    }

    public void delete(){
        this.withdrawal="Y";
    }

    public String getRoleKey() {
        return this.role.getRole();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
