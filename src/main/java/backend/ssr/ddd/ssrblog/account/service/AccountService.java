package backend.ssr.ddd.ssrblog.account.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.account.dto.profile.AccountProfileRequest;
import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import backend.ssr.ddd.ssrblog.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Account getAccountProfile(Long accountIdx) {

        return accountRepository.findByAccountIdxAndWithdrawal(accountIdx, "N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));
    }

    public Account updateAccountProfile(String email, String platform, AccountProfileRequest accountProfileRequest) {

        Account account = accountRepository.findByEmailAndPlatformAndWithdrawal(email, platform, "N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));

        account.updateProfile(accountProfileRequest);

        return accountRepository.save(account);
    }

    public String getReissueToken(String refreshToken) {
        log.info("Get client refreshToken : {}", refreshToken);

        String reIssueToken = jwtTokenProvider.reIssueToken(refreshToken);
        log.info("reIssueToken : {}", reIssueToken);

        return reIssueToken;
    }

    public Account getAccountInfo(String email, String platform) {

        return accountRepository.findByEmailAndPlatformAndWithdrawal(email, platform, "N").orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));
    }

    public void delete(Long accountIdx){
        Account account = accountRepository.findById(accountIdx).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));

        account.delete();

        accountRepository.save(account);
    }

}
