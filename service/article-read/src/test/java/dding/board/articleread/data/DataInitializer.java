package dding.board.articleread.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.random.RandomGenerator;

public class DataInitializer {
    RestClient articleServiceClient = RestClient.create("http://localhost:9000");
    RestClient commentServiceClient = RestClient.create("http://localhost:9001");
    RestClient lIkiServiceClient = RestClient.create("http://localhost:9002");
    RestClient viewServiceClient = RestClient.create("http://localhost:9003");



    @Test
    void initialize()
    {
        for(int i= 0 ; i<30; i++)
        {
            Long articleId = createArticle();
            System.out.println("articleId = " +articleId);
            long commentCount = RandomGenerator.getDefault().nextLong(10);
            long likeCount = RandomGenerator.getDefault().nextLong(10);
            long viewCount = RandomGenerator.getDefault().nextLong(200);

            createComment(articleId,commentCount);
            like(articleId,likeCount);
            view(articleId,viewCount);
        }
    }

    private void createComment(Long articleId, long commentCount) {
        while(commentCount --> 0)
        {
            commentServiceClient.post()
                    .uri("v1/comments")
                    .body(new CommentCreateRequest(articleId,"content", 1L))
                    .retrieve();
        }
    }

    private void like(Long articleId, long likeCount)
    {
        while(likeCount --> 0 )
        {
            lIkiServiceClient.post()
                    .uri("/v1/article-likes/articles/{articleID}/users/{userId}/pessimistic-lock-1", articleId, likeCount)
                    .retrieve();
        }
    }

    private void view(Long articleId, long viewCount)
    {
        while(viewCount --> 0 )
        {
            viewServiceClient.post()
                    .uri("v1/article-views/articles/{articleId}/users/{userId}",articleId,viewCount)
                    .retrieve();
        }
    }

    Long createArticle()
    {
        return articleServiceClient.post()
                .uri("/v1/articles")
                .body(new ArticleCreateRequest("title","content",1L,1L))
                .retrieve()
                .body(ArticleResponse.class)
                .getArticleId();
    }


    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest
    {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }
    @Getter
    static class ArticleResponse{
        private Long articleId;
        private String title;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    static class CommentCreateRequest{
        private long articleId;
        private String content;
        private Long writerId;
    }
}
