package dding.board.articleread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@EntityScan(basePackages = "dding.board")
@ComponentScan(basePackages = "dding.board")
@SpringBootApplication
public class ArticleReadApplication {
    public static void  main(String[] args)
    {
        SpringApplication.run(ArticleReadApplication.class, args);
    }
}
