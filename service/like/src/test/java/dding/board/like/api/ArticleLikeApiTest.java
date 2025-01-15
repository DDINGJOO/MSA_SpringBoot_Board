package dding.board.like.api;


import dding.board.like.dto.Response.ArticleLikeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ArticleLikeApiTest {

    RestClient restClient = RestClient.create("http://localhost:9002");


    @Test
    void LikeAndUnLikeTest()
    {
        Long articleId = 9999L;
        like(articleId, 1L);
        like(articleId, 2L);
        like(articleId, 3L);

        ArticleLikeResponse response1 = read(articleId, 1L);
        System.out.println("response1 = " + response1 );
        ArticleLikeResponse response2 = read(articleId, 2L);
        System.out.println("response2 = " + response2 );
        ArticleLikeResponse response3 = read(articleId, 3L);
        System.out.println("response3 = " + response3 );

        unLike(articleId, 1L);
        System.out.println("response1 after unLike  = " + response1);




    }

    void like(Long articleId, Long userId)
    {
        restClient.post()
                .uri("/v1/article-likes/articles/{articleId}/users/{userId}",articleId, userId)
                .retrieve();
    }


    void unLike(Long articleId, Long userId)
    {
        restClient.delete()
                .uri("/v1/article-likes/articles/{articleId}/users/{userId}",articleId, userId)
                .retrieve();
    }

    ArticleLikeResponse read(Long articleId, Long userId)
    {
        return restClient.get()
                .uri("/v1/article-likes/articles/{articleId}/users/{userId}",articleId, userId)
                .retrieve()
                .body(ArticleLikeResponse.class);
    }
}
