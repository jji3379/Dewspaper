package backend.ssr.ddd.ssrblog.account.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    private final JwtTokenProvider jwtTokenProvider;
    public Account getAccountOne(Long idx) {

        return accountRepository.findById(idx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));
    }

    public String getReissueToken(String refreshToken) {
        log.info("Get client refreshToken : {}", refreshToken);

        String reIssueToken = jwtTokenProvider.reIssueToken(refreshToken);
        log.info("reIssueToken : {}", reIssueToken);

        return reIssueToken;
    }

}
