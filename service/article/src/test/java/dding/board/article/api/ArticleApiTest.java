package dding.board.article.api;

import dding.board.article.dto.response.ArticlePageResponse;
import dding.board.article.dto.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

import static java.lang.module.ModuleDescriptor.read;

public class ArticleApiTest {
   RestClient restClient = RestClient.create("http://localhost:9000");



    @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest(
                "hi", "my content", 1L, 1L
        ));
        System.out.println("response = " + response);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }


    @Test
    void readTest() {
        ArticleResponse response = read(FactoryArticle().getArticleId());
        System.out.println("response = " + response);
    }

    ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/articles/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void readAllTest()
    {
        ArticlePageResponse response = restClient.get()
                .uri("/v1/articles?boardId=1&pageSize=30&page=100")
                .retrieve()
                .body(ArticlePageResponse.class);

        System.out.println("response.getArticleCount() = "+ response.getArticleCount());
        for(ArticleResponse article : response.getArticles())
        {
            System.out.println("article = " + article.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollInitPageTest() {
        var article1 = restClient.get()
                .uri("v1/articles/infinite-scroll?boardId=1&pageSize=7")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        System.out.println("firstPage");
        for (ArticleResponse articleResponse : article1) {
            System.out.println("articleResponse.getArticleId() = " + articleResponse.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollNextPageTest()
    {
        var article1 = restClient.get()
                .uri("v1/articles/infinite-scroll?boardId=1&pageSize=7")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        var lastArticleId = article1.getLast().getArticleId();
        System.out.printf("lastArticleId = %s%n", lastArticleId);
        var article2 = restClient.get()
                .uri("v1/articles/infinite-scroll?boardId=1&pageSize=7&lastArticleId=%s".formatted(lastArticleId))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        System.out.println("SecondPage");
        for(ArticleResponse articleResponse : article2)
        {
            System.out.println("articleResponse.getArticleId() = " + articleResponse.getArticleId());
        }

    }

    @Test
    void readAllInfiniteScrollContinueousTest()
    {
        var article1 = restClient.get()
                .uri("v1/articles/infinite-scroll?boardId=1&pageSize=7")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        var lastArticleId1 = article1.getLast().getArticleId();
        System.out.printf("lastArticleId = %s%n", lastArticleId1);
        var article2 = restClient.get()
                .uri("v1/articles/infinite-scroll?boardId=1&pageSize=8")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        var lastArticleId2 = article2.getLast().getArticleId();
        System.out.printf("nextArticleId = %s%n", lastArticleId2);

        var article3 = restClient.get()
                .uri("v1/articles/infinite-scroll?boardId=1&pageSize=7&lastArticleId=%s".formatted(lastArticleId1))
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });
        Assertions.assertThat(lastArticleId2).isEqualTo(article3.getFirst().getArticleId());
    }


    @Test
    void updateTest()
    {
        ArticleResponse factoryArticle = FactoryArticle();
        System.out.println("before = " + factoryArticle);
        var before = factoryArticle.getModifiedAt();
        factoryArticle = update(factoryArticle.getArticleId(), new ArticleUpdateRequest("hi2", "hihi2"));
        System.out.println("response = "+ factoryArticle);
        Assertions.assertThat(factoryArticle.getModifiedAt()).isAfter(before);
    }

    ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
        return restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void deleteTest()
    {
        ArticleResponse factoryArticle = FactoryArticle();
        restClient.delete()
                .uri("v1/articles/{articleId}", factoryArticle.getArticleId())
                .retrieve();
    }









    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long boardId; //shard key
        private Long writerId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }

    ArticleResponse FactoryArticle()
    {
        return create(new ArticleCreateRequest("Title","Content",1L,1L));
    }



}


