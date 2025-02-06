package dding.board.articleread.client;


import io.lettuce.core.dynamic.annotation.CommandNaming;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import javax.naming.LinkLoopException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleClient {

    private RestClient restClient;

    @Value("${endpoints.dding-board-article-service.url}")
    private String articleServiceUrl;


    @PostConstruct
    public void initRestClient()
    {
        restClient = RestClient.create(articleServiceUrl);
    }

    public Optional<ArticleResponse> read(Long articleId)
    {
        try{
            var articleResponse = restClient.get()
                    .uri("/v1/articles/{articleId}",articleId)
                    .retrieve()
                    .body(ArticleResponse.class);
            return Optional.ofNullable(articleResponse);
        }catch (Exception e)
        {
            log.error("[ArticleClient.read] articleId = {} ",articleId,e);
            return  Optional.empty();
        }
    }

    @Getter
    public static class ArticleResponse{
        private Long articleId;
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
