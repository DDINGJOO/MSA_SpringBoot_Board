package dding.board.hotarticle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class HotArticleApplication {
    public static void  main(String[] args)
    {
        SpringApplication.run(HotArticleApplication.class, args);
    }
}
