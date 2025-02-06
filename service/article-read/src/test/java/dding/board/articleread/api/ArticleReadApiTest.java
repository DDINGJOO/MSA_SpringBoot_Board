package dding.board.articleread.api;


import dding.board.articleread.dto.response.ArticleReadPageResponse;
import dding.board.articleread.dto.response.ArticleReadResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

public class ArticleReadApiTest {
    RestClient articleReadrestClient = RestClient.create("http://localhost:9005");
    RestClient articleRestClient = RestClient.create("http://localhost:9000");

    @Test
    void readTest()
    {
        var response = articleReadrestClient.get()
                .uri("/v1/articles/{articleId}",145722962428489728L)
                .retrieve()
                .body(ArticleReadResponse.class);
        System.out.println("response = "  +response);
    }

    @Test
    void readAllTest()
    {
        var response = articleReadrestClient.get()
                .uri("/v1/articles?boardId=%s&page=%s&pageSize=%s".formatted(1L,1L,5L))
                .retrieve()
                .body(ArticleReadPageResponse.class);

        System.out.println("response1 count = " + response.getArticleCount());


        var response2 = articleRestClient.get()
                .uri("/v1/articles?boardId=%s&page=%s&pageSize=%s".formatted(1L,1L,5L))
                .retrieve()
                .body(ArticleReadPageResponse.class);
        System.out.println("response2 count = " + response2.getArticleCount());


        for(int i =0 ; i< response.getArticleCount() ; i++)
        {
            if(!Objects.equals(response.getArticles().get(i).getArticleId(), response2.getArticles().get(i).getArticleId()))
            {
                System.out.println("not equal");
            }
            Assertions.assertThat(response.getArticles().get(i).getArticleId()).isEqualTo(response2.getArticles().get(i).getArticleId());
        }
        System.out.println("ALL equal ><");

    }




    @Test
    void readAllInfiniteScrollTest()
    {
        List<ArticleReadResponse> response1 = articleReadrestClient.get()
                .uri("/v1/articles/infinite-Scroll?boardId=%s&pageSize=%s".formatted(1L,5L))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>(){});
        ;


        List<ArticleReadResponse> response2= articleRestClient.get()
                .uri("/v1/articles/infinite-Scroll?boardId=%s&pageSize=%s".formatted(1L,5L))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>(){});

        for(int i= 0; i< response2.size();i++)
        {
            Assertions.assertThat(response1.size()).isEqualTo(response2.size());
        }
    }

    @Test
    void readAllInfiniteScrollTestContinuous()
    {
        List<ArticleReadResponse> response1 = articleReadrestClient.get()
                .uri("/v1/articles/infinite-Scroll?boardId=%s&lastArticleId = %s&pageSize=%s".formatted(1L,145759244879060992L,5L))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>(){});
        ;


        List<ArticleReadResponse> response2= articleRestClient.get()
                .uri("/v1/articles/infinite-Scroll?boardId=%s&lastArticleId = %s&pageSize=%s".formatted(1L,145759244879060992L,5L))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>(){});

        for(int i= 0; i< response2.size();i++)
        {
            Assertions.assertThat(response1.size()).isEqualTo(response2.size());
        }
    }



}
