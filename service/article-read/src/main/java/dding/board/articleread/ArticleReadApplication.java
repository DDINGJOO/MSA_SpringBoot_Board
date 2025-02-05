package dding.board.articleread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "msa_board")
@SpringBootApplication
@EnableJpaRepositories(basePackages = "dding.board")
public class ArticleReadApplication {
    public static void  main(String[] args)
    {
        SpringApplication.run(ArticleReadApplication.class, args);
    }
}
