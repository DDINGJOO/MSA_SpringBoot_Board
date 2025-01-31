package dding.board.view.repository.service;

import dding.board.view.entity.ArticleViewCount;
import dding.board.view.repository.ArticleViewCountBackUpRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ArticleViewCountBackUpRepositoryTest {

    @Autowired
    ArticleViewCountBackUpRepository articleViewCountRepository;

    @PersistenceContext
    EntityManager entityManager;


    @Test
    @Transactional
    void updateViewCount() {
        //given
        articleViewCountRepository.save(
                ArticleViewCount.init(1L)
        );

        entityManager.flush();
        entityManager.clear();

        //when
        var result1 = articleViewCountRepository.updateViewCount(1L,100L);
        var result2 = articleViewCountRepository.updateViewCount(1L,200L);
        var result3 = articleViewCountRepository.updateViewCount(1L,100L);


        //then

        Assertions.assertThat(result1).isEqualTo(1);
        Assertions.assertThat(result2).isEqualTo(1);
        Assertions.assertThat(result3).isEqualTo(0); //더 작은 값은 업데이트 -> return 0

        ArticleViewCount articleViewCount = articleViewCountRepository.findById(1L).get();
        Assertions.assertThat(articleViewCount.getViewCount()).isEqualTo(200L);

    }
}