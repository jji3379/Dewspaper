package backend.ssr.ddd.ssrblog.config;

import io.swagger.annotations.ApiParam;
import lombok.Getter;

import javax.annotation.Nullable;

@Getter
public class SwaggerPageable {
    @ApiParam(value = "현재 페이지의 수 (0..N, 기본값 : 0)")
    @Nullable
    private Integer page;

    @ApiParam(value = "페이지 크기 (1~500 ,기본값 : 5)")
    @Nullable
    private Integer size;

    @ApiParam(value = "정렬 (컬럼명,ASC|DESC ,기본값 : createDate,ASC)")
    @Nullable
    private String sort;
}
