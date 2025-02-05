package dding.board.hotarticle.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class HotArticleListRepositoryTest {

    @Autowired
    HotArticleListRepository hotArticleListRepository;

    @Test
    void addTest() throws InterruptedException {
        //Given
        LocalDateTime time = LocalDateTime.of(2025,2,5,0,0);
        System.out.println(time.toString());
        long limit = 3L;

        //when


        hotArticleListRepository.add(1L,time, 3L,limit, Duration.ofSeconds(5));
        hotArticleListRepository.add(2L,time, 4L,limit, Duration.ofSeconds(5));
        hotArticleListRepository.add(3L,time, 5L,limit, Duration.ofSeconds(5));
        hotArticleListRepository.add(4L,time, 2L,limit, Duration.ofSeconds(5));
        hotArticleListRepository.add(5L,time, 1L,limit, Duration.ofSeconds(5));
        // RANK : 3- 2- 1- 4-5

        //then
        List<Long> articleIds = hotArticleListRepository.readAll("20250205");

        Assertions.assertThat(articleIds).hasSize(Long.valueOf(limit).intValue());
        Assertions.assertThat(articleIds.get(0)).isEqualTo(3);
        Assertions.assertThat(articleIds.get(1)).isEqualTo(2);
        Assertions.assertThat(articleIds.get(2)).isEqualTo(1);

        TimeUnit.SECONDS.sleep(5);
        Assertions.assertThat(hotArticleListRepository.readAll("20250205")).isEmpty();


    }

    @Test
    void readTest()
    {
        List<Long> articlesId = hotArticleListRepository.readAll("20250205");
        System.out.println("err line");
        for(long ids: articlesId)
        {
            System.out.println(ids);
        }
    }
}