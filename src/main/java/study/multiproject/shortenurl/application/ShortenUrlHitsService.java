package study.multiproject.shortenurl.application;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortenUrlHitsService {

    private static final String HITS_KEY = "redirect:count";
    private final RedisTemplate<String, String> redisTemplate;


    /**
     * shortKey에 대응하는 redirectCount를 1 증가시킵니다.
     */
    public void incrementHit(String shortKey) {
        // Redis Hash 의 field 로 shortKey, value 로 count 를 저장
        redisTemplate.opsForHash().increment(HITS_KEY, shortKey, 1L);
    }

    /**
     * originalUrl을 조회
     */
    public Optional<String> getHits(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    /**
     * originalUrl을 저장
     */
    public void putToSet(String key, String url) {
        redisTemplate.opsForValue().set(key, url, expirationDuration().getSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 만료 시간 계산
     */
    private Duration expirationDuration() {
        // 현재 시간 부터 00:00:00까지의 시간 계산
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, now.plusDays(1).withHour(0).withMinute(0).withSecond(0));
    }

    public Map<String, Long> fetchAllHits() {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(HITS_KEY);
        return entries.entrySet().stream()
                   .collect(Collectors.toMap(
                       e -> e.getKey().toString(),
                       e -> Long.parseLong(e.getValue().toString())
                   ));
    }

    /**
     * 배치가 끝난 뒤 Redis 카운터를 초기화(혹은 삭제)합니다.
     */
    public void resetAllHits() {
        redisTemplate.delete(HITS_KEY);
    }
}
