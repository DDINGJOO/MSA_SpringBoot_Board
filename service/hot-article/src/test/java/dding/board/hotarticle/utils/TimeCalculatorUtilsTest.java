package dding.board.hotarticle.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


class TimeCalculatorUtilsTest {

    @Test
    void calculateDurationToMidnight() {
        Duration duration = TimeCalculatorUtils.calculateDurationToMidnight();
        System.out.println("duration.getSeconds() /60  = "+ duration.getSeconds()/60);
    }
}