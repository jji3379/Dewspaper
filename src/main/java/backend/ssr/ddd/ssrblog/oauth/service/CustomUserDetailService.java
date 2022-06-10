package backend.ssr.ddd.ssrblog.oauth.service;

import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService {

    private final AccountRepository accountRepository;

    public UserDetails loadUserByUsername(String username, String platform) throws UsernameNotFoundException {
        return accountRepository.findByEmailAndPlatform(username, platform)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
    }
}