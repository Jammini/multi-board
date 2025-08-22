package study.multiproject.post.application;

import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalVisitorDeduper {

    private final CacheManager cacheManager;

    /**
     * 방문자가 오늘 처음 조회한 게시글인지 확인하고, 오늘 처음 조회한 경우에만 true를 반환한다.
     */
    public boolean markIfFirstToday(long postId, String visitorId) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache("viewDedupe");
        Cache<Object, Object> caffeineCache = Objects.requireNonNull(cache).getNativeCache();

        // 키 생성
        String key = generateKey(postId, visitorId).toString();
        Set<Long> visitors = (Set<Long>) caffeineCache.getIfPresent(key);

        // 오늘 처음 조회한 경우
        if (visitors == null) {
            visitors = ConcurrentHashMap.newKeySet();
            caffeineCache.put(key, visitors);
        }
        return visitors.add(Long.parseLong(visitorId));
    }
}
