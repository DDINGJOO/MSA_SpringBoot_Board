package dding.board.view.repository.Api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewApiTest
{
    RestClient restClient = RestClient.create("http://localhost:9003");


    @Test
    void viewTest() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(10000);


        for (int i = 0; i < 10000; i++) {
            long j = i;
            executorService.submit(() -> {
                restClient.post()
                        .uri("/v1/article-views/articles/{articleId}/users/{userId}", 3L, j)
                                .retrieve();
                latch.countDown();

            });
        }

        latch.await();

        var count = restClient.get()
                .uri("/v1/article-views/articles/{articleId}/count",3L)
                .retrieve()
                .body(Long.class);
        System.out.println(count);


    }
}
