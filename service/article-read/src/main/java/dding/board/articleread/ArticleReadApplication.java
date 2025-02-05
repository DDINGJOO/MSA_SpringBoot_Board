package dding.board.articleread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EntityScan(basePackages = "msa_board")
@SpringBootApplication
public class ArticleReadApplication {
    public static void  main(String[] args)
    {
        SpringApplication.run(ArticleReadApplication.class, args);
    }
}
