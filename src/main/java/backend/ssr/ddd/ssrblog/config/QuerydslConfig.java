package backend.ssr.ddd.ssrblog.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {
    @PersistenceContext
    private EntityManager entityManager;

    // JPAQueryFactory 를 주입받기 위한 빈 등록
    @Bean
    /*
     * QueryDSL 을 사용하여 쿼리를 build 하기 위해서 필요한 JPAQueryFactory
     * JPAQueryFactory 를 사용하면 EntityManager 를 통해서 질의가 처리되고, JPQL 을 사용한다.
     */
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
