package backend.ssr.ddd.ssrblog.account.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import backend.ssr.ddd.ssrblog.oauth.mapper.AccountRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRequestMapper accountRequestMapper;
    public Account getAccountOne(Long idx) {

        return accountRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 회원의 정보가 없습니다."));
    }

    public String getReissueToken(String refreshToken) {

        Account userInfo = accountRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 토큰이 존재하지 않습니다."));

        String accessToken = jwtTokenProvider.createToken(userInfo.getEmail(), userInfo.getRole().getRole(), userInfo.getPlatform());

        return accessToken;
    }

}
