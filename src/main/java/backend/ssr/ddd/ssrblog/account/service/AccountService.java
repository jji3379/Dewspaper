package backend.ssr.ddd.ssrblog.account.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final JwtTokenProvider jwtTokenProvider;
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
