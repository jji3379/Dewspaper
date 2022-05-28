package backend.ssr.ddd.ssrblog.controller;

import backend.ssr.ddd.ssrblog.domain.entity.Account;
import backend.ssr.ddd.ssrblog.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/test")
    public List<Account> test() {
        return accountService.getAccount();
    }
}
