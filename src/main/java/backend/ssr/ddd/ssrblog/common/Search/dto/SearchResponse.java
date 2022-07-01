package backend.ssr.ddd.ssrblog.common.Search.dto;

import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchResponse {
    private List<AccountResponse> accounts;
    private List<PostResponse> posts;

    @Builder
    public SearchResponse(List<AccountResponse> accounts, List<PostResponse> posts) {
        this.accounts = accounts;
        this.posts = posts;
    }

    public SearchResponse getSearchAccountsAndPosts(List<AccountResponse> accounts, List<PostResponse> posts) {
        this.accounts = accounts;
        this.posts = posts;

        return this;
    }
}
