package backend.ssr.ddd.ssrblog.account.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("GUEST","손님"),
    USER("USER","일반 사용자");

    private final String role;
    private final String title;
}
