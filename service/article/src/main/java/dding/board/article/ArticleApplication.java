package dding.board.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackages = "dding.board")
@SpringBootApplication
@ComponentScan(basePackages = "dding.board")
@EnableJpaRepositories(basePackages = "dding.board")
public class ArticleApplication {
    public static void  main(String[] args)
    {
        SpringApplication.run(ArticleApplication.class, args);
    }
}
