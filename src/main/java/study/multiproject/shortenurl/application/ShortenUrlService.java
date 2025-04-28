package study.multiproject.shortenurl.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.shortenurl.application.request.ShortenUrlCreateServiceRequest;
import study.multiproject.shortenurl.application.response.ShortenUrlInformationResponse;
import study.multiproject.shortenurl.application.response.ShortenUrlResponse;
import study.multiproject.shortenurl.dao.ShortenUrlRepository;
import study.multiproject.shortenurl.domain.ShortenUrl;
import study.multiproject.shortenurl.exception.NotFoundShortenUrlException;

@Service
@RequiredArgsConstructor
public class ShortenUrlService {

    private final ShortenUrlRepository shortenUrlRepository;

    /**
     * ShortenUrl을 생성한다.
     */
    @Transactional
    public ShortenUrlResponse generateShortenUrl(ShortenUrlCreateServiceRequest request) {
        String originalUrl = request.originalUrl();

        // 이전에 이미 생성된 적이 있는 URL이면 따로 생성하지 않고 기존 값을 재활용
        Optional<ShortenUrl> existing = shortenUrlRepository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            return new ShortenUrlResponse(existing.get());
        }
        // 없으면 생성
        ShortenUrl shortenUrl = shortenUrlRepository.save(ShortenUrl.builder()
                                                        .originalUrl(originalUrl)
                                                        .build());

        String shortenUrlKey = ShortenUrl.generate(shortenUrl.getId());
        shortenUrl.saveShortenUrlKey(shortenUrlKey);
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
        ShortenUrl shortenUrl = shortenUrlRepository.findByShortenUrlKey(shortenUrlKey).orElseThrow(
            NotFoundShortenUrlException::new);
        shortenUrl.increaseRedirectCount();
        return shortenUrl.getOriginalUrl();
    }
}
