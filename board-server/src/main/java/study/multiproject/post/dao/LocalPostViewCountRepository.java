package study.multiproject.post.dao;

import static study.multiproject.global.util.PostCountConstants.LOCAL_CACHE_MANAGER;
import static study.multiproject.global.util.PostCountConstants.POST_VIEW_COUNT_KEY;
import static study.multiproject.global.util.PostCountConstants.VIEW_DEDUPE_LOCAL;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LocalPostViewCountRepository implements PostViewCountRepository {

    private final Cache<String, Long> localViewCounterCache;

    @Override
    @Cacheable(
        cacheManager = LOCAL_CACHE_MANAGER,
        cacheNames = VIEW_DEDUPE_LOCAL,
        key = "'post:' + #postId + '|user:' + #userId"
    )
    public Long increaseData(long postId, long userId) {
        return localViewCounterCache.asMap().merge(POST_VIEW_COUNT_KEY + postId, 1L, Long::sum);
    }

    @Override
    public Set<String> getAllKeys() {
        return new HashSet<>(localViewCounterCache.asMap().keySet());
    }

    @Override
    public Long getViewCount(String key) {
        return localViewCounterCache.getIfPresent(key);
    }

    @Override
    public void deleteKey(String key) {
        localViewCounterCache.invalidate(key);
    }
}
