package backend.ssr.ddd.ssrblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SsrBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsrBlogApplication.class, args);
    }

}
