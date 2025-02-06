package dding.board.articleread.client;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class ViewClientTest {

    @Autowired
    ViewClient viewClient;


    @Test
    void readableTest() throws InterruptedException {
        viewClient.count(4L);
        viewClient.count(4L);
        viewClient.count(4L);

        TimeUnit.SECONDS.sleep(5);
        viewClient.count(4L);
    }
}