package backend.ssr.ddd.ssrblog.oauth.mapper;

import backend.ssr.ddd.ssrblog.account.dto.AccountRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AccountRequestMapper {
    public AccountRequest toDto(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        return AccountRequest.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .platform((String) attributes.get("platform"))
                .role((String) attributes.get("role"))
                .build();
    }
}