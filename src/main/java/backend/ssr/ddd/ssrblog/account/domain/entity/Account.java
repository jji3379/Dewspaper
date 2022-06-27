package backend.ssr.ddd.ssrblog.account.domain.entity;

import backend.ssr.ddd.ssrblog.account.dto.AccountMeResponse;
import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.account.dto.Role;
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
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_idx")
    private Long accountIdx;

    private String profileImg;

    private String platform;

    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String withdrawal = "N";

    @Builder
    public Account(Long accountIdx, String profileImg, String platform, String email, String name, Role role) {
        this.accountIdx = accountIdx;
        this.profileImg = profileImg;
        this.platform = platform;
        this.email = email;
        this.name = name;
        this.role = role;
    }
    public AccountResponse toResponse() {
        AccountResponse build = AccountResponse.builder()
                .accountIdx(accountIdx)
                .platform(platform)
                .email(email)
                .name(name)
                .role(role.getRole())
                .profileImg(profileImg)
                .withdrawal(withdrawal)
                .build();
        return build;
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
