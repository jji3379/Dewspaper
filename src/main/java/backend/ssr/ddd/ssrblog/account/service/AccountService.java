package backend.ssr.ddd.ssrblog.account.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.account.dto.profile.AccountProfileRequest;
import backend.ssr.ddd.ssrblog.account.dto.profile.AccountProfileResponse;
import backend.ssr.ddd.ssrblog.comment.domain.entity.Comment;
import backend.ssr.ddd.ssrblog.comment.domain.repository.CommentRepository;
import backend.ssr.ddd.ssrblog.comment.service.CommentService;
import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import backend.ssr.ddd.ssrblog.friends.domain.repository.FriendsRepository;
import backend.ssr.ddd.ssrblog.friends.service.FriendService;
import backend.ssr.ddd.ssrblog.oauth.jwt.JwtTokenProvider;
import backend.ssr.ddd.ssrblog.post.domain.repository.PostRepository;
import backend.ssr.ddd.ssrblog.writer.domain.entity.Writer;
import backend.ssr.ddd.ssrblog.writer.domain.repository.WriterRepository;
import backend.ssr.ddd.ssrblog.writer.service.WriterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommentService commentService;
    private final FriendService friendService;
    private final WriterService writerService;
    private final CommentRepository commentRepository;
    private final WriterRepository writerRepository;
    private final FriendsRepository friendsRepository;
    private final PostRepository postRepository;

    public AccountProfileResponse getAccountProfile(Account accountIdx, Authentication authentication) {
        boolean owner = false; // 블로그의 주인을 확인
        boolean isCrew = false; // 크루 여부

        Account accountProfile = accountRepository.findByAccountIdxAndWithdrawal(accountIdx.getAccountIdx(), "N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));

        if (authentication != null) { // 토큰이 있을 경우
            Account tokenValue = (Account) authentication.getPrincipal();
            if (accountProfile.getEmail().equals(tokenValue.getEmail()) && accountProfile.getPlatform().equals(tokenValue.getPlatform())) { // 이메일과 플랫폼이 요청한 accountIdx 의 회원과 같을 경우
                owner = true; // 주인임을 나타냄
            }
            List<Account> friendsList = friendService.getFriendsList(tokenValue);

            if (friendsList.contains(accountIdx)) {
                isCrew = true;
            }
        }

        long postCount = writerService.getAccountPostCount(accountIdx); // 내가 작성한 글의 개수
        long commentCount = commentService.getAccountCommentCount(accountIdx); // 내가 작성한 댓글의 개수
        long friendsCount = friendService.getAccountFriendsCount(accountIdx); // 내 친구 수

        return accountProfile.toProfileResponse(postCount, commentCount, friendsCount, owner, isCrew);
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

    @Transactional
    public void deleteAccount(Authentication authentication){
        Account tokenValue = (Account) authentication.getPrincipal();

        Account account = accountRepository.findByEmailAndPlatformAndWithdrawal(tokenValue.getEmail(), tokenValue.getPlatform(), "N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));

        // 댓글 삭제
        List<Comment> myCommentList = commentRepository.findByAccountIdxAndDelYn(account, "N");
        for (Comment comment : myCommentList) {
            comment.delete();
            commentRepository.save(comment);
        }

        // 글 삭제
        List<Writer> myPostList = writerRepository.findByAccountIdxAndDelYn(account, "N");
        for (Writer writer : myPostList) {
            writer.delete();
            writerRepository.save(writer);
            // cowriter 가 하나도 없을 경우 -> 혼자 작성한 글 삭제
            long coWriterCount = writerRepository.countByPostIdxAndDelYn(writer.getPostIdx(), "N");
            if (coWriterCount < 1) {
                writer.getPostIdx().delete();
            }
        }

        // 친구 목록 삭제
        List<Friends> myFriendsList = friendsRepository.findByRequesterIdxOrAccepterIdx(account, account);
        for (Friends friend: myFriendsList) {
            friendsRepository.delete(friend);
        }

        account.delete();

        accountRepository.save(account);
    }

}
