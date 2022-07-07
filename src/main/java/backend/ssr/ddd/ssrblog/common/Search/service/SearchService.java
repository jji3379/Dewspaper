package backend.ssr.ddd.ssrblog.common.Search.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.entity.QAccount;
import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.post.domain.entity.QPost;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import backend.ssr.ddd.ssrblog.post.service.PostService;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final JPAQueryFactory jpaQueryFactory;

    private final PostService postService;
    /**
     * 회원, 게시물 동시 검색
     */
    public Page<AccountResponse> searchAccount(Pageable pageable, String search) {
        QAccount qAccount = QAccount.account;

        QueryResults<Account> accounts = jpaQueryFactory.select(qAccount)
                .from(qAccount)
                .where(qAccount.email.contains(search)
                        .or(qAccount.name.contains(search)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AccountResponse> accountResponseList = new ArrayList<>();

        for (Account account : accounts.getResults()) {
            accountResponseList.add(account.toResponse());
        }

        return new PageImpl<>(accountResponseList, pageable, accounts.getTotal());
    }

    public Page<PostResponse> searchPost(Pageable pageable, String search) {
        QPost qPost = QPost.post;

        QueryResults<Post> posts = jpaQueryFactory.select(qPost)
                .from(qPost)
                .where(qPost.title.contains(search)
                        .or(qPost.contents.contains(search)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<PostResponse> postResponseList = new ArrayList<>();
        for (Post post : posts.getResults()) {
            postResponseList.add(post.toResponse(postService.getCoWriterInfo(post)));
        }

        return new PageImpl<>(postResponseList, pageable, posts.getTotal());
    }
}
