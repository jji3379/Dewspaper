package backend.ssr.ddd.ssrblog.account.controller;

import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.service.AccountService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "1. 회원 API")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/test")
    public List<Account> test() {
        return accountService.getAccount();
    }

    @GetMapping("/api/{idx}")
    public AccountResponse getAccount(@PathVariable Long idx) {
        return accountService.getAccountOne(idx).toResponse();
    }

    @PostMapping("/authtest")
    public String authTest() {
        return "testContent";
    }
}
