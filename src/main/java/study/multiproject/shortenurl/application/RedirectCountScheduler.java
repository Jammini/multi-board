package study.multiproject.shortenurl.application;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.shortenurl.dao.ShortenUrlRepository;

@Component
@RequiredArgsConstructor
public class RedirectCountScheduler {

    private final ShortenUrlHitsService hitsService;
    private final ShortenUrlRepository shortenUrlRepository;

    @Scheduled(fixedRate = 60_000)  // 1분마다
    @Transactional
    public void flushHitsToDb() {
        Map<String, Long> allHits = hitsService.fetchAllHits();
        if (allHits.isEmpty()) {
            return;
        }

        allHits.forEach((shortKey, count) -> {
            shortenUrlRepository.findByShortenUrlKey(shortKey).ifPresent(u -> {
                u.addRedirectCount(count);
            });
        });

        // 반영 완료되면 Redis 초기화
        hitsService.resetAllHits();
    }

}
