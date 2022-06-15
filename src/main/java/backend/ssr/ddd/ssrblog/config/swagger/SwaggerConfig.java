package backend.ssr.ddd.ssrblog.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    // Docket : Swagger 설정의 핵심이 되는 Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2) // DocumentationType 을 SWAGGER2 를 사용하고 있기에 SWAGGER_2 로 한다
                .apiInfo(apiInfo()) // api 문서의 기본 정보
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select() // ApiSelectorBuilder 를 생성
                .apis(RequestHandlerSelectors.basePackage("backend.ssr.ddd.ssrblog")) // api 스펙이 작성되어 있는 패키지 (Controller) 를 지정
                .paths(PathSelectors.any()) // apis 에 있는 API 중 특정 path 를 선택
                .build()
                .directModelSubstitute(Pageable.class, SwaggerPageable.class);
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "authorization", "header");
    }

    private SecurityContext securityContext() {
        return springfox
                .documentation
                .spi.service
                .contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }


    // Api 의 기본 문서 정보
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder() // Api 문서의 정보를 builder 형식으로 추가해서 반환한다.
                .title("Dewspaper API 문서") // 제목
                .version("v1") // 버전 정보
                .description("회원, 게시글,  API 문서") // 설명
                .build();
    }
}
