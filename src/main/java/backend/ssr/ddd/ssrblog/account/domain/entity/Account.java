package backend.ssr.ddd.ssrblog.account.domain.entity;

import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.account.dto.Role;
import backend.ssr.ddd.ssrblog.account.dto.friends.AccountFriendsResponse;
import backend.ssr.ddd.ssrblog.account.dto.profile.AccountProfileAlarmRequest;
import backend.ssr.ddd.ssrblog.account.dto.profile.AccountProfileRequest;
import backend.ssr.ddd.ssrblog.account.dto.profile.AccountProfileResponse;
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

    private String profileId;

    private String platform;

    private String email;

    private String name;

    private String blogName;

    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String introduction;

    private String emailAgree = "N";

    private String alarmAgree = "N";

    private String withdrawal = "N";

    public AccountResponse toResponse() {
        AccountResponse build = AccountResponse.builder()
                .accountIdx(accountIdx)
                .profileId(profileId)
                .platform(platform)
                .email(email)
                .name(name)
                .blogName(blogName)
                .profileImg(profileImg)
                .role(role.getRole())
                .introduction(introduction)
                .emailAgree(emailAgree)
                .alarmAgree(alarmAgree)
                .createDate(getCreateDate())
                .updateDate(getUpdateDate())
                .withdrawal(withdrawal)
                .build();
        return build;
    }

    // 친구 목록
    public AccountFriendsResponse toFriendsResponse() {
        AccountFriendsResponse build = AccountFriendsResponse.builder()
                .accountIdx(accountIdx)
                .name(name)
                .profileImg(profileImg)
                .build();
        return build;
    }

    public AccountProfileResponse toProfileResponse(long postCount, long commentCount, long crewCount, boolean owner, boolean isCrew) {
        AccountProfileResponse build = AccountProfileResponse.builder()
                .accountIdx(accountIdx)
                .profileId(profileId)
                .email(email)
                .platform(platform)
                .name(name)
                .blogName(blogName)
                .role(role.getRole())
                .profileImg(profileImg)
                .introduction(introduction)
                .emailAgree(emailAgree)
                .alarmAgree(alarmAgree)
                .owner(owner)
                .postCount(postCount)
                .commentCount(commentCount)
                .crewCount(crewCount)
                .isCrew(isCrew)
                .build();

        return build;
    }

    @Builder
    public Account(Long accountIdx, String profileId, String platform, String email, String name, String blogName, String profileImg, Role role, String introduction, String emailAgree, String alarmAgree, String withdrawal) {
        this.accountIdx = accountIdx;
        this.profileId = profileId;
        this.platform = platform;
        this.email = email;
        this.name = name;
        this.blogName = blogName;
        this.profileImg = profileImg;
        this.role = role;
        this.introduction = introduction;
        this.emailAgree = emailAgree;
        this.alarmAgree = alarmAgree;
        this.withdrawal = withdrawal;
    }

    public Account update(String platform, String name) {
        this.platform = platform;
        this.name = name;

        return this;
    }

    public Account updateProfile(AccountProfileRequest accountProfileRequest) {
        this.name = accountProfileRequest.getName();
        this.profileImg = accountProfileRequest.getProfileImg();
        this.blogName = accountProfileRequest.getBlogName();
        this.introduction = accountProfileRequest.getIntroduction();

        return this;
    }

    public Account updateProfileAlarm(AccountProfileAlarmRequest accountProfileAlarmRequest) {
        this.alarmAgree = accountProfileAlarmRequest.getAlarmAgree();
        this.emailAgree = accountProfileAlarmRequest.getEmailAgree();

        return this;
    }

    public void delete(){
        this.withdrawal="Y";
    }

    public Account setDefault(String profileId) {
        this.emailAgree = "N";
        this.alarmAgree = "N";
        this.withdrawal = "N";
        this.profileId = profileId;
        this.blogName = name + "님의 블로그";

        return this;
    }

    // -----------------------------------------------

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
