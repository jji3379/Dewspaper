package backend.ssr.ddd.ssrblog.common.Search.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.entity.QAccount;
import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.common.Search.dto.SearchResponse;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.post.domain.entity.QPost;
import backend.ssr.ddd.ssrblog.post.domain.repository.PostRepository;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final JPAQueryFactory jpaQueryFactory;
    /**
     * 회원, 게시물 동시 검색
     */
    public SearchResponse searchAccountAndPost(String search) {
        QAccount qAccount = QAccount.account;
        QPost qPost = QPost.post;

        List<Account> accounts = jpaQueryFactory.select(qAccount)
                .from(qAccount)
                .where(qAccount.email.contains(search)
                        .or(qAccount.name.contains(search)))
                .fetch();

        List<Post> posts = jpaQueryFactory.select(qPost)
                .from(qPost)
                .where(qPost.title.contains(search)
                        .or(qPost.contents.contains(search)))
                .fetch();

        SearchResponse searchResponse = new SearchResponse();

        List<AccountResponse> accountResponseList = new ArrayList<>();
        List<PostResponse> postResponseList = new ArrayList<>();

        for (Account account : accounts) {
            accountResponseList.add(account.toResponse());
        }

        for (Post post : posts) {
            postResponseList.add(post.toResponse());
        }

        searchResponse.getSearchAccountsAndPosts(accountResponseList, postResponseList);


        return searchResponse;
    }
}
