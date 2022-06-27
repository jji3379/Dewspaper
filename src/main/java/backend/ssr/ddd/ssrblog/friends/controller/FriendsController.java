package backend.ssr.ddd.ssrblog.friends.controller;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsRequest;
import backend.ssr.ddd.ssrblog.friends.dto.FriendsResponse;
import backend.ssr.ddd.ssrblog.friends.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "3. 친구 목록 API")
public class FriendsController {

    private final FriendService friendService;

    private final AccountRepository accountRepository;

    /**
     * 친구 목록 테스트용 전체 회원 조회
     */
    @GetMapping("/all-accounts") @ApiOperation(value = "테스트용 전체 회원 조회", notes = "친구 목록 테스트용 전체 회원 조회 API 입니다.")
    public List<AccountResponse> getAllAccounts() {
        List<Account> all = accountRepository.findAll();

        List<AccountResponse> responseList = new ArrayList<>();

        for (Account account : all) {
            responseList.add(account.toResponse());
        }

        return responseList;

    }

    /**
     * 친구 목록
     */
    @GetMapping("/friends/{accountIdx}") @ApiOperation(value = "친구 목록 리스트", notes = "친구 목록 리스트 (요청 보내고 수락 받은 요청, 요청 받고 수락한 요청 다 포함)")
    @ApiImplicitParam(name = "accountIdx", required = true, value = "예 : 8")
    public ResponseEntity<List<AccountResponse>> getFriendsList(@PathVariable Account accountIdx) {
        List<Account> getFriendsList = friendService.getFriendsList(accountIdx);

        List<AccountResponse> responseList = new ArrayList<>();

        for (Account friends : getFriendsList) {
            responseList.add(friends.toResponse());
        }

        return new ResponseEntity(responseList, HttpStatus.OK);
    }

    /**
     * 친구 요청 받은 목록 리스트
     */
    @GetMapping("/friends/{accepterIdx}/required") @ApiOperation(value = "친구 요청 받은 목록 리스트", notes = "요청 받은 목록 리스트 (수락해줘야 하기에 accpeterIdx 을 입력)")
    @ApiImplicitParam(name = "accepterIdx", required = true, value = "예 : 8")
    public ResponseEntity<List<FriendsResponse>> getRequiredFriendsList(@PathVariable Account accepterIdx) {
        List<Friends> requiredFriendsList = friendService.getRequiredFriendsList(accepterIdx);

        List<FriendsResponse> responseList = new ArrayList<>();

        for (Friends friends : requiredFriendsList) {
            responseList.add(friends.toResponse());
        }

        return new ResponseEntity(responseList, HttpStatus.OK);
    }

    /**
     * 내가 보낸 친구 요청 리스트
     */
    @GetMapping("/friends/{requesterIdx}/require") @ApiOperation(value = "내가 보낸 친구 요청 리스트", notes = "내가 보낸 (요청한) 목록 이기에  requesterIdx 을 입력)")
    @ApiImplicitParam(name = "requesterIdx", required = true, value = "예 : 11")
    public ResponseEntity<List<FriendsResponse>> getRequireToFriendsList(@PathVariable Account requesterIdx) {
        List<Friends> requireToFriendsList = friendService.getRequireToFriendsList(requesterIdx);

        List<FriendsResponse> responseList = new ArrayList<>();

        for (Friends friends : requireToFriendsList) {
            responseList.add(friends.toResponse());
        }

        return new ResponseEntity(responseList, HttpStatus.OK);
    }

    /**
     * 친구 요청
     */
    @PostMapping("/friends") @ApiOperation(value = "친구 요청", notes = "requesterIdx 와 accepterIdx 를 입력하여 새로운 친구 요청을 보낸다.")
    public ResponseEntity<FriendsResponse> newFriendRequest(@RequestBody FriendsRequest friendsRequest) {
        Friends newFriend = friendService.newFriendRequest(friendsRequest);

        return new ResponseEntity(newFriend.toResponse(), HttpStatus.CREATED);
    }

    /**
     * 친구 수락
     */
    @PutMapping("/friends") @ApiOperation(value = "친구 수락", notes = "requesterIdx 와 accepterIdx 를 입력하여 친구 요청을 수락한다.")
    public ResponseEntity<FriendsResponse> acceptFriend(@RequestBody FriendsRequest friendsRequest) {
        Friends friends = friendService.acceptFriend(friendsRequest);

        return new ResponseEntity(friends.toResponse(), HttpStatus.OK);
    }

    /**
     * 친구 삭제
     */
    @DeleteMapping("/friends/{requesterIdx}/{accepterIdx}") @ApiOperation(value = "친구 삭제", notes = "requesterIdx 와 accepterIdx 를 입력하여 친구를 삭제한다. (순서 상관 없음, 요청을 보내는 중일 때 1번 더 클릭시, 친구 상태에서 1번 더 클릭시 삭제)")
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "requesterIdx", required = true, value = "예 : 8"),
            @ApiImplicitParam(name = "accepterIdx", required = true, value = "예 : 11")
        }
    )
    public void deleteFriend(@PathVariable Account requesterIdx, @PathVariable Account accepterIdx) {

        friendService.deleteFriend(requesterIdx, accepterIdx);
    }

}
