package backend.ssr.ddd.ssrblog.account.controller;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.account.dto.profile.AccountProfileAlarmRequest;
import backend.ssr.ddd.ssrblog.account.dto.profile.AccountProfileRequest;
import backend.ssr.ddd.ssrblog.account.dto.profile.AccountProfileResponse;
import backend.ssr.ddd.ssrblog.account.service.AccountService;
import backend.ssr.ddd.ssrblog.comment.service.CommentService;
import backend.ssr.ddd.ssrblog.friends.service.FriendService;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import backend.ssr.ddd.ssrblog.post.service.PostService;
import backend.ssr.ddd.ssrblog.writer.service.WriterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@Api(tags = "1. 회원 API")
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final PostService postService;

    @GetMapping("/account/{accountIdx}") @ApiOperation(value = "회원 정보 조회", notes = "회원의 상세 정보를 조회 한다. <br>전체 글의 수, 댓글의 수, 크루의 수 등 포함.  <br>다른 사람도 나의 정보를 볼 수 있기에 idx 포함.  <br>토큰값과 idx 회원이 같을 경우 owner : true  <br>크루로 등록되어 있을 경우 crew : true")
    @ApiImplicitParam(name = "accountIdx", value = "8", required = true)
    public ResponseEntity<AccountProfileResponse> getAccountProfile(@PathVariable Account accountIdx, Authentication authentication) {
        AccountProfileResponse accountProfile = accountService.getAccountProfile(accountIdx, authentication);

        return new ResponseEntity<>(accountProfile, HttpStatus.OK);
    }

    @GetMapping("/account/posts/{display}") @ApiOperation(value = "내가 쓴 글 보기 (공개, 비공개)", notes = "내가 작성한 공개 또는 비공개 글 목록을 조회 한다. (나의 글이기 때문에 idx 값이 아닌 '토큰으로만' 판별한다.)")
    @ApiImplicitParam(name = "display", value = "public(공개), private(비공개)", required = true)
    public ResponseEntity<Page<PostResponse>> getMyPostList(Authentication authentication, Pageable pageable, @PathVariable String display) {
        Account account = (Account) authentication.getPrincipal();

        Page<PostResponse> myPosts = postService.getMyPosts(pageable, account, display);

        return new ResponseEntity<>(myPosts, HttpStatus.OK);
    }

    @GetMapping("/account/{accountIdx}/posts/{tab}") @ApiOperation(value = "메인 페이지 나의 글 목록 (내가 작성한 글, 태그된 글)", notes = "내가 작성한 글 또는 태그된 글을 보여준다. \n 내가 작성한 글은 tab 에 post \n 태그된 글은 tab 에 tag 입력한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountIdx", value = "8", required = true),
            @ApiImplicitParam(name = "tab", value = "post(내가 작성한 글), tag(태그된 글)", required = true)
    })
    public ResponseEntity<Page<PostResponse>> getPostListOrTagList(Pageable pageable, @PathVariable Account accountIdx, @PathVariable String tab) {
        Page<PostResponse> myPostsOrTagList = postService.getMyPostsOrTagList(pageable, accountIdx, tab);

        return new ResponseEntity<>(myPostsOrTagList, HttpStatus.OK);
    }

    @PutMapping("/account/profile") @ApiOperation(value = "회원 정보 수정", notes = "회원의 정보를 수정 한다. (이름, 블로그 이름, 소개)")
    public ResponseEntity<AccountResponse> updateAccountProfile(Authentication authentication, @RequestBody AccountProfileRequest accountProfileRequest) {
        Account account = (Account) authentication.getPrincipal();

        Account updateAccount = accountService.updateAccountProfile(account.getEmail(), account.getPlatform(), accountProfileRequest);

        return new ResponseEntity<>(updateAccount.toResponse(), HttpStatus.OK);
    }

    @PutMapping("/account/alarm") @ApiOperation(value = "이메일 수신 설정", notes = "이메일과 알림 수신 설정을 수정한다.")
    public ResponseEntity<AccountResponse> updateAccountProfileAlarm(Authentication authentication, @RequestBody AccountProfileAlarmRequest accountProfileAlarmRequest) {
        Account account = (Account) authentication.getPrincipal();

        Account updateAccount = accountService.updateAccountProfileAlarm(account.getEmail(), account.getPlatform(), accountProfileAlarmRequest);

        return new ResponseEntity<>(updateAccount.toResponse(), HttpStatus.OK);
    }

    @DeleteMapping("/account") @ApiOperation(value = "회원 정보 삭제", notes = "토큰 정보로 삭제할 회원을 구별한다. <br>회원을 삭제할 시 회원이 작성한 글, 댓글, 친구 목록이 삭제된다. <br>삭제되는 회원이 realWriter 일 경우, coWriter 가 남아있을 경우 글은 삭제되지 않는다. <br>하지만, 혼자 작성한 글의 경우 (cowriter 가 본인만 있을 경우) 는 글도 삭제된다.")
    public ResponseEntity<AccountProfileResponse> deleteAccount(Authentication authentication) {
        accountService.deleteAccount(authentication);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh-token") @ApiOperation(value = "토큰 재발급", notes = "refresh-token 을 header 에 입력하여 새로운 토큰을 발급 받는다.")
    @ApiImplicitParam(name = "refreshToken", value = "예 : {\"refreshToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqamkzMzc5QGdtYWlsLmNvbSIsInJvbGVzIjoiVVNFUiIsImlzcyI6Imdvb2dsZSIsImlhdCI6MTY1ODA2NTk1OCwiZXhwIjoxNjU5Mjc1NTU4fQ.52zdQpAq46uR8bY9D_7lXFICDbKIYrAk-Jyx8DSCrmQ\"}", required = true)
    public String getRefreshToken(@RequestBody Map<String, String> refreshToken) {
        String reissueToken = accountService.getReissueToken(refreshToken.get("refreshToken"));

        return reissueToken;
    }

    @GetMapping("/test")
    public String tokenTest() {

        return "토큰토큰";
    }

    @GetMapping("/me")
    @ApiOperation(value = "회원 기본 정보 조회", notes = "회원의 상세 정보를 조회 한다. (기본 회원의 정보만을 조회한다.)")
    public ResponseEntity<AccountResponse> getAccountInfo(Authentication authentication) {
        Account account = (Account) authentication.getPrincipal();

        return new ResponseEntity<>(accountService.getAccountInfo(account.getEmail(), account.getPlatform()).toResponse(), HttpStatus.OK);
    }
}
