package dding.board.articleread.api;


import dding.board.articleread.dto.response.ArticleReadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

public class ArticleReadApiTest {
    RestClient restClient = RestClient.create("http://localhost:9005");

    @Test
    void readTest()
    {
        var response = restClient.get()
                .uri("/v1/articles/{articleId}",145722962428489728L)
                .retrieve()
                .body(ArticleReadResponse.class);
        System.out.println("response = "  +response);
    }


}
