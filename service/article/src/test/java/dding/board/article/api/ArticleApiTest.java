package dding.board.article.api;

import dding.board.article.dto.response.ArticlePageResponse;
import dding.board.article.dto.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

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


