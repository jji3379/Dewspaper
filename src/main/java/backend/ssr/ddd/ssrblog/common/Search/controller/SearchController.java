package backend.ssr.ddd.ssrblog.common.Search.controller;

import backend.ssr.ddd.ssrblog.common.Search.dto.SearchResponse;
import backend.ssr.ddd.ssrblog.common.Search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/search/{search}") @ApiOperation(value = "게시물, 회원 검색 조회", notes = "게시물 제목, 내용 / 회원의 이름, 이메일 에 검색어가 포함된 결과를 조회한다. Response 는 accounts, posts 로 반환된다.")
    @ApiImplicitParam(name = "search", value = "예 : j")
    public SearchResponse getSearchAccountsAndPosts(@PathVariable String search) {
        searchService.searchAccountAndPost(search);

        return searchService.searchAccountAndPost(search);
    }
}
