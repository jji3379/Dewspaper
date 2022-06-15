package backend.ssr.ddd.ssrblog.account.controller;

import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.account.service.AccountService;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtResponse;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Api(tags = "1. 회원 API")
public class AccountController {

    private final AccountService accountService;

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/api/{idx}")
    public AccountResponse getAccount(@PathVariable Long idx) {

        return accountService.getAccountOne(idx).toResponse();
    }

    @PostMapping("/refresh-token")
    @ApiImplicitParam(name = "REFRESH-TOKEN", value = "refresh-token", required = true, dataType = "String", paramType = "header")
    public String getRefreshToken(@RequestHeader(value="REFRESH-TOKEN") String refreshToken) {

        String reissueToken = accountService.getReissueToken(refreshToken);

        return reissueToken;
    }

}
