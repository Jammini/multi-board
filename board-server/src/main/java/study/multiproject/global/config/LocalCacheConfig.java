package study.multiproject.global.config;

import static study.multiproject.global.util.PostCountConstants.VIEW_DEDUPE_LOCAL;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalCacheConfig {

    @Bean
    public CaffeineCacheManager localCacheManager() {
        CaffeineCacheManager m = new CaffeineCacheManager(VIEW_DEDUPE_LOCAL);
        m.setAllowNullValues(false);
        m.setCaffeine(
            Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(24))
                .maximumSize(500_000)
        );
        return m;
    }

    @Bean
    public Cache<String, Long> localCaffeine() {
        return Caffeine.newBuilder()
                   .expireAfterWrite(Duration.ofDays(1))
                   .maximumSize(100_000)
                   .build();
    }
}
