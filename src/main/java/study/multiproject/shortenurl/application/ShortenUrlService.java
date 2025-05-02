package study.multiproject.shortenurl.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.shortenurl.application.request.ShortenUrlCreateServiceRequest;
import study.multiproject.shortenurl.application.response.ShortenUrlInformationResponse;
import study.multiproject.shortenurl.application.response.ShortenUrlResponse;
import study.multiproject.shortenurl.common.Base62;
import study.multiproject.shortenurl.dao.ShortenUrlRepository;
import study.multiproject.shortenurl.domain.ShortenUrl;
import study.multiproject.shortenurl.exception.NotFoundShortenUrlException;

@Service
@RequiredArgsConstructor
public class ShortenUrlService {

    private final Base62 base62;
    private final ShortenUrlRepository shortenUrlRepository;
    private final ShortenUrlHitsService shortenUrlHitsService;

    /**
     * ShortenUrl을 생성한다.
     */
    @Transactional
    public ShortenUrlResponse generateShortenUrl(ShortenUrlCreateServiceRequest request) {
        String originalUrl = request.originalUrl();

        // 1) 캐시에 저장된 originalUrl이 있는지 확인
        Optional<String> cached = shortenUrlHitsService.getHits(originalUrl);
        if (cached.isPresent()) {
            return new ShortenUrlResponse(originalUrl, cached.get());
        }

        // 2) 캐시에 없으면 DB에서 조회
        ShortenUrl shortenUrl = shortenUrlRepository.findByOriginalUrl(originalUrl)
                                .orElseGet(() -> {
                                    ShortenUrl u = shortenUrlRepository.save(ShortenUrl.builder()
                                                                                 .originalUrl(originalUrl)
                                                                                 .build());
                                    String key = base62.encode(ShortenUrl.generate(u.getId()));
                                    u.saveShortenUrlKey(key);
                                    return u;
                                });
        // 3) 캐시 매핑
        cacheMapping(shortenUrl);
        return new ShortenUrlResponse(shortenUrl);

    }

    /**
     * ShortenUrl 정보를 조회한다.
     */
    @Transactional(readOnly = true)
    public ShortenUrlInformationResponse getShortenUrlInformationByShortenUrlKey(String shortenUrlKey) {
        ShortenUrl shortenUrl = shortenUrlRepository.findByShortenUrlKey(shortenUrlKey).orElseThrow(
            NotFoundShortenUrlException::new);
        return new ShortenUrlInformationResponse(shortenUrl);
    }

    /**
     * 리다이렉트 횟수를 증가한다.
     */
    @Transactional
    public String getOriginalUrlByShortenUrlKey(String shortenUrlKey) {

        // 1) 캐시 우선 조회 → 없으면 DB 조회 후 캐시 저장
        String originalUrl = shortenUrlHitsService.getHits(shortenUrlKey)
                                 .orElseGet(() -> {
                                     ShortenUrl url = shortenUrlRepository.findByShortenUrlKey(shortenUrlKey)
                                                      .orElseThrow(NotFoundShortenUrlException::new);
                                     cacheMapping(url);
                                     return url.getOriginalUrl();
                                 });
        // 2) 리다이렉트 횟수 증가
        shortenUrlHitsService.incrementHit(shortenUrlKey);
        return originalUrl;
    }

    /**
     * 캐시 매핑
     */
    private void cacheMapping(ShortenUrl shortenUrl) {
        shortenUrlHitsService.putToSet(shortenUrl.getOriginalUrl(), shortenUrl.getShortenUrlKey());
        shortenUrlHitsService.putToSet(shortenUrl.getShortenUrlKey(), shortenUrl.getOriginalUrl());
    }
}
