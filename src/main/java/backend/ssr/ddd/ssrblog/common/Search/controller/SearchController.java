package backend.ssr.ddd.ssrblog.common.Search.controller;

import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.common.Search.service.SearchService;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Api(tags = "6. 검색 API")
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search/accounts/{search}") @ApiOperation(value = "게시물, 회원 검색 조회", notes = "회원의 이름, 이메일에 검색어가 포함된 결과를 조회한다.")
    @ApiImplicitParam(name = "search", value = "예 : j")
    public ResponseEntity<Page<AccountResponse>> getSearchAccounts(Pageable pageable, @PathVariable String search) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());

        Page<AccountResponse> accountResponses = searchService.searchAccount(pageable, search);

        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }

    @GetMapping("/search/posts/{search}") @ApiOperation(value = "게시물, 회원 검색 조회", notes = "게시물 제목, 내용에 검색어가 포함된 결과를 조회한다.")
    @ApiImplicitParam(name = "search", value = "예 : s")
    public ResponseEntity<Page<PostResponse>> getSearchPosts(Pageable pageable, @PathVariable String search) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());

        Page<PostResponse> postResponses = searchService.searchPost(pageable, search);

        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }
}
