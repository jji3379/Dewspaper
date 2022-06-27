package backend.ssr.ddd.ssrblog.account.controller;

import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.account.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@Api(tags = "1. 회원 API")
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account/{accountIdx}") @ApiOperation(value = "회원 정보 조회", notes = "회원의 상세 정보를 조회 한다.")
    @ApiImplicitParam(name = "accountIdx", required = true, value = "예 : 1")
    public AccountResponse getAccount(@PathVariable Long accountIdx) {

        return accountService.getAccountOne(accountIdx).toResponse();
    }

    @PostMapping("/refresh-token") @ApiOperation(value = "토큰 재발급", notes = "refresh-token 을 header 에 입력하여 새로운 토큰을 발급 받는다.")
    @ApiImplicitParam(name = "refreshToken", value = "예 : {\"refreshToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqamkzMzc5QGdtYWlsLmNvbSIsInJvbGVzIjoiVVNFUiIsImlzcyI6Imdvb2dsZSIsImlhdCI6MTY1NjMwMTk3OSwiZXhwIjoxNjU2MzMwNzc5fQ.tGys2dWW0sdDtKKsoBolnth1VDTda-ORFPDVci-yIqM\"}", required = true)
    public String getRefreshToken(@RequestBody Map<String, String> refreshToken) {
        String reissueToken = accountService.getReissueToken(refreshToken.get("refreshToken"));

        return reissueToken;
    }

    @GetMapping("/test")
    public String tokenTest() {

        return "토큰토큰";
    }

}
