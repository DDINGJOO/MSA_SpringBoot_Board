package dding.board.articleread.cache;

import lombok.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class OptimizedCacheTest {

    @Test
    void parseDateTest(){
        parseDateTest("data", 10);
        parseDateTest(3L, 10);
        parseDateTest(3, 10);
        parseDateTest(new TestClass("hi"),10);
    }

    void parseDateTest(Object data ,long ttlSecond)
    {
        //given
        OptimizedCache optimizedCache = OptimizedCache.of(data, Duration.ofSeconds(ttlSecond));
        System.out.println("optimizedCache = " + optimizedCache);


        //when
        Object resolveData = optimizedCache.parseData(data.getClass());

        //then
        System.out.println("resolvedData = " +resolveData);
        Assertions.assertThat(resolveData).isEqualTo(data);
    }

    @Test
    void isExpiredTest()
    {
        Assertions.assertThat(OptimizedCache.of("Data", Duration.ofDays(-30)).isExpired()).isTrue();
        Assertions.assertThat(OptimizedCache.of("Data", Duration.ofDays(30)).isExpired()).isFalse();

    }


    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    static class TestClass{
        String testData;

    }

}