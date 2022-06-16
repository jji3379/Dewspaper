package backend.ssr.ddd.ssrblog.account.controller;

import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.account.service.AccountService;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtResponse;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Api(tags = "1. 회원 API")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/api/{idx}")
    public AccountResponse getAccount(@PathVariable Long idx) {

        return accountService.getAccountOne(idx).toResponse();
    }

    @PostMapping("/api/refresh-token") @ApiOperation(value = "토큰 재발급", notes = "refresh-token 을 header 에 입력하여 새로운 토큰을 발급 받는다.")
    @ApiImplicitParam(name = "refresh-token", value = "예 : eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqamkzMzc5QG5hdmVyLmNvbSIsImlzcyI6Im5hdmVyIiwiaWF0IjoxNjU1Mzk0OTY4LCJleHAiOjE2NTU5OTk3Njh9.j-HAHp05U6WG20dH9gVtqB64e5TAB7ECliqEhuVVCcM", required = true, dataType = "String", paramType = "header")
    public String getRefreshToken(@RequestHeader(value="refresh-token") String refreshToken) {

        String reissueToken = accountService.getReissueToken(refreshToken);

        return reissueToken;
    }

}
