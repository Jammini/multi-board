package study.multiproject.post.application;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.multiproject.post.dao.PostHitsRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostHitsService {

    private final PostHitsRepository postHitsRepository;
    private final LocalVisitorDeduper localDeduper;
    private final LocalViewBuffer localViewBuffer;

    /**
     * 방문자 중복 체크 후 최초 방문이면 조회수 증가 + 방문자 등록
     * Redis 장애/지연 시 fallback으로 스킵
     */
    @CircuitBreaker(name = "CircuitBreakerConfig", fallbackMethod = "firstVisitFallback")
    public void viewCountIncrease(String visitorId, long postId) {
        visitRedis(visitorId, postId);
    }

    /**
     * 주요 서비스에서 Redis 장애/지연 시 호출되는 fallback 메서드
     */
    public void firstVisitFallback(String visitorId, long postId, Throwable t) {
        if (localDeduper.markIfFirstToday(postId, visitorId)) {
            localViewBuffer.increase(postId);
        }
        log.warn("Redis unavailable -> buffered locally. visitorId={}, postId={}, cause={}",
            visitorId, postId, (t != null ? t.toString() : "n/a"));
    }

    /**
     * Redis에 방문자 등록 및 조회수 증가
     */
    private void visitRedis(String visitorId, long postId) {
        if (!postHitsRepository.isExistInSet(visitorId, postId)) {
            postHitsRepository.increaseData("viewCount_post_" + postId);
            postHitsRepository.addToSet(visitorId, postId);
        }
    }
}
