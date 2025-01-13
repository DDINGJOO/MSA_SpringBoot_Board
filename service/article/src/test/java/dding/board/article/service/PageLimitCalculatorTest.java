package dding.board.article.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;



class PageLimitCalculatorTest {

    @Test
    void calculatePageLimitTest(){
        calculatePageLimitTest(1L, 30L, 10L, 301L);
        calculatePageLimitTest(2L, 30L, 10L, 301L);
        calculatePageLimitTest(10L, 30L, 10L, 301L);
        calculatePageLimitTest(11L,30L, 10L, 601L);
        calculatePageLimitTest(17L, 30L, 10L, 601L);
    }

    void calculatePageLimitTest(Long page, Long pageSize, Long movablePageCount, Long expected)
    {
        var result = PageLimitCalculator.calculator(page,pageSize,movablePageCount);
        Assertions.assertThat(result).isEqualTo(expected);
    }
}