package study.multiproject.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Caffeine 캐시 설정
     */
    @Bean
    Caffeine<Object, Object> caffeine() {
        return Caffeine.newBuilder()
                   .expireAfterWrite(Duration.ofDays(1))
                   .maximumSize(100_000);
    }

    /**
     * Caffeine 캐시 매니저 설정
     */
    @Bean
    CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager manager = new CaffeineCacheManager("viewDedupe");
        manager.setCaffeine(caffeine);
        return manager;
    }
}
