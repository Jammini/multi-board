package study.multiproject.post.application;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.multiproject.post.dao.LocalPostViewCountRepository;
import study.multiproject.post.dao.RedisPostViewCountRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostViewCountService {

    private final RedisPostViewCountRepository redisRepository;
    private final LocalPostViewCountRepository localRepository;
    /**
     * 방문자 중복 체크 후 조회수 증가
     */
    @CircuitBreaker(name = "CircuitBreakerConfig", fallbackMethod = "increaseViewCountFallback")
    public void increaseViewCount(long postId, long userId) {
        redisRepository.increaseData(postId, userId);
    }

    /**
     * fallback 메서드
     */
    public void increaseViewCountFallback(long postId, long userId, Throwable t) {
        localRepository.increaseData(postId, userId);
        log.warn("Redis unavailable -> buffered locally. postId={}, userId={}, cause={}",
            postId, userId, (t != null ? t.toString() : "n/a"));
    }
}
