package study.multiproject.post.application;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostHitsCircuitService {

    private final PostHitsService postHitsService;
    private final LocalViewBuffer localViewBuffer;

    /**
     * 방문자 중복 체크 후 최초 방문이면 조회수 증가 + 방문자 등록
     * Redis 장애/지연 시 fallback으로 스킵
     */
    @CircuitBreaker(name = "CircuitBreakerConfig", fallbackMethod = "firstVisitFallback")
    public void visitRedis(String visitorId, long postId) {
        if (!postHitsService.isExistInSet(visitorId, postId)) {
            postHitsService.increaseData("viewCount_post_" + postId);
            postHitsService.addToSet(visitorId, postId);
        }
    }

    /**
     * 주요 서비스에서 Redis 장애/지연 시 호출되는 fallback 메서드
     */
    public void firstVisitFallback(String visitorId, long postId, Throwable t) {
        localViewBuffer.increase(postId);
        log.warn("Redis unavailable -> buffered locally. visitorId={}, postId={}, cause={}",
            visitorId, postId, (t != null ? t.toString() : "n/a"));
    }
}
