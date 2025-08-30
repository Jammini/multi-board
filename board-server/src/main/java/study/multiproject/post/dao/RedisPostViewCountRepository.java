package study.multiproject.post.dao;

import static study.multiproject.global.util.PostCountConstants.POST_VIEW_COUNT_KEY;
import static study.multiproject.global.util.PostCountConstants.REDIS_CACHE_MANAGER;
import static study.multiproject.global.util.PostCountConstants.VIEW_DEDUPE;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisPostViewCountRepository implements PostViewCountRepository {

    private final RedisTemplate<String, Long> redisTemplate;

    /**
     * 조회수 증가
     */
    @Override
    @Cacheable(
        cacheManager = REDIS_CACHE_MANAGER,
        cacheNames = VIEW_DEDUPE,
        key = "'post:' + #postId + '|user:' + #userId"
    )
    public Long increaseData(long postId, long userId) {
        return redisTemplate.opsForValue().increment(POST_VIEW_COUNT_KEY + postId);
    }

    /**
     * 모든 조회수 키 조회
     */
    @Override
    public Set<String> getAllKeys() {
        return redisTemplate.keys(POST_VIEW_COUNT_KEY + "*");
    }

    /**
     * 조회수 조회
     */
    @Override
    public Long getViewCount(String key) {
        Number value = redisTemplate.opsForValue().get(key);
        return value != null ? value.longValue() : 0L;
    }

    /**
     * 키 삭제
     */
    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
