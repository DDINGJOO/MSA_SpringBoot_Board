package dding.board.article.repository;

import dding.board.article.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;



@Slf4j
@SpringBootTest
class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;

    @Test
    void findAllTest(){
        List<Article> articles = articleRepository.findAll(1L, 10L, 30L);
        log.info("article.size = {}", articles.size());
        for(Article article : articles)
        {
            log.info("article = {}",article);
        }
    }

    @Test
    void findInfiniteScrollTest()
    {
        List<Article> articles = articleRepository.findAllInfiniteScroll(1L,30L);
        log.info("article.size = {}",articles.size());
        for(Article article : articles)
        {
            log.info("article = {}", article);
        }
    }

    @Test
    void findNextInfiniteScrollTest()
    {
        var lastArticleId = articleRepository.findAllInfiniteScroll(1L,30L).getLast().getArticleId();
        List<Article> articles = articleRepository.findAllInfiniteScroll(1L,30L,lastArticleId);
        Assertions.assertThat(30L).isEqualTo(articles.size());
        for(Article article : articles)
        {
            log.info("article = {}", article);
        }

    }


    @Test
    void countTest(){
        Long count = articleRepository.count(1L, 10000L);
        log.info("count = {}", count);
    }

}