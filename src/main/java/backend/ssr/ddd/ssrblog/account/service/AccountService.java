package backend.ssr.ddd.ssrblog.account.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
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
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));
    }

    public String getReissueToken(String refreshToken) {

        Account userInfo = accountRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        String accessToken = jwtTokenProvider.createToken(userInfo.getEmail(), userInfo.getRole().getRole(), userInfo.getPlatform());

        return accessToken;
    }

}
