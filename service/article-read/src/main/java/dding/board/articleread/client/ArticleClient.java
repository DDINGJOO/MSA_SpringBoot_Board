package dding.board.articleread.client;


import io.lettuce.core.dynamic.annotation.CommandNaming;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import javax.naming.LinkLoopException;
import java.time.LocalDateTime;
import java.util.List;
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


    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize)
    {
        try{
            return restClient.get()
                    .uri("/v1/articles?boardId=%s&page=%s&pageSize=%s".formatted(boardId, page, pageSize))
                    .retrieve()
                    .body(ArticlePageResponse.class);
        }catch (Exception e) {
            log.error("[ArticleClient.readALl] boardId = {}, page ={}, pageSize = {}", boardId, page,page, e);
            return ArticlePageResponse.EMPTY;
        }
    }

    public List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long lastArticleId, Long pageSize)
    {
        try{
            return restClient.get()
                    .uri(
                            lastArticleId != null?
                                    "/v1/articles/infinite-scroll?boardId=%s&lastArticleId=%s&pageSize=%s"
                                            .formatted(boardId,articleServiceUrl,pageSize) :
                                    "/v1/articles/infinite-scroll?boardId=%s&pageSize=%s"
                                            .formatted(boardId,pageSize)
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ArticleResponse>>() {});
        }catch (Exception e)
        {
            log.error("[ArticleClient.readAllInfiniteScroll] boardId = {}, lastArticleId ={}, pageSize = {}",
                    boardId,lastArticleId,pageSize);
            return  List.of();
        }
    }


    public Long count( long boardId) {
        try{
            return restClient.get()
                    .uri("/v1/articles/boards/{boardId}/count",boardId)
                    .retrieve()
                    .body(Long.class);
        }catch (Exception e)
        {
            log.error("[ArticleClient.count] boardId=  {}", boardId, e);
            return 0L;
        }
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticlePageResponse{
        private List<ArticleResponse> articles;
        private Long articleCount;

        public static ArticlePageResponse EMPTY = new ArticlePageResponse(List.of(), 0L);

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
