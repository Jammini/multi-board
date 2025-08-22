package study.multiproject.post.dao;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostHitsRepositoryImpl implements PostHitsRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 조회수 증가
     */
    public void increaseData(String visitorId) {
        redisTemplate.opsForValue().increment(visitorId);
    }

    /**
     * 조회수 저장
     */
    public void addToSet(String visitorId, Long postId) {
        redisTemplate.opsForSet().add(visitorId, String.valueOf(postId));
        redisTemplate.expire(visitorId, expirationDuration().getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 조회수 존재 여부 확인
     */
    public boolean isExistInSet(String visitorId, Long postId) {
        return Boolean.TRUE.equals(
            redisTemplate.opsForSet().isMember(visitorId, String.valueOf(postId)));
    }

    /**
     * 모든 조회수 키 조회
     */
    public Set<String> getAllKeys() {
        return redisTemplate.keys("viewCount_post_*");
    }

    /**
     * 조회수 조회
     */
    public Integer getHits(String key) {
        return (Integer) redisTemplate.opsForValue().get(key);
    }

    /**
     * 키 삭제
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 만료 시간 계산
     */
    private Duration expirationDuration() {
        // 현재 시간 부터 00:00:00까지의 시간 계산
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, now.plusDays(1).withHour(0).withMinute(0).withSecond(0));
    }
}
