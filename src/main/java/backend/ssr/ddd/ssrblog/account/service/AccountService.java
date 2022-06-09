package backend.ssr.ddd.ssrblog.service;

import backend.ssr.ddd.ssrblog.domain.entity.Account;
import backend.ssr.ddd.ssrblog.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> getAccount() {
        return accountRepository.findAll();
    }

    public Account getAccountOne(Long idx) {
        return accountRepository.findById(idx).get();
    }

}
