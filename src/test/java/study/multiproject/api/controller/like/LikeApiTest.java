package study.multiproject.api.controller.like;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;
import study.multiproject.api.config.TestSecurityConfig;
import study.multiproject.like.application.response.LikeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestSecurityConfig.class)
public class LikeApiTest {

    RestClient restClient = RestClient.create("http://localhost:8080");

    @Test
    void likePerformanceTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        likePerformanceTest(executorService, 1L, "pessimistic-lock-1");
        likePerformanceTest(executorService, 2L, "pessimistic-lock-2");
        likePerformanceTest(executorService, 3L, "optimistic-lock");
    }

    void likePerformanceTest(ExecutorService executorService, Long postId, String lockType)
        throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(3000);
        System.out.println(lockType + " start");

        like(postId, 1L, lockType);

        long start = System.nanoTime();
        for (int i = 0; i < 3000; i++) {
            long userId = i + 2;
            executorService.submit(() -> {
                like(postId, userId, lockType);
                latch.countDown();
            });
        }
        latch.await();

        long end = System.nanoTime();
        System.out.println("lockType = " + lockType + ", time = " + (end -start) / 1000000 + "ms");
        System.out.println(lockType + " end");

        Long count = restClient.get()
                         .uri("/likes/posts/{postId}/count", postId)
                         .retrieve()
                         .body(Long.class);

        System.out.println("count = " + count);
    }

    void like(Long postId, Long userId, String lockType) {
        restClient.post()
            .uri("/likes/posts/{postId}/users/{userId}/" + lockType, postId, userId)
            .retrieve();
    }

    void unlike(Long postId, Long userId) {
        restClient.post()
            .uri("/likes/posts/{postId}/users/{userId}/pessimistic-lock-1", postId, userId)
            .retrieve();
    }

    LikeResponse read(Long postId, Long userId) {
        return restClient.get()
                   .uri("/likes/posts/{postId}/users/{userId}", postId, userId)
                   .retrieve()
                   .body(LikeResponse.class);
    }
}
