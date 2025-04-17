package study.multiproject.shortenurl.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.shortenurl.application.request.ShortenUrlCreateServiceRequest;
import study.multiproject.shortenurl.application.response.ShortenUrlInformationResponse;
import study.multiproject.shortenurl.application.response.ShortenUrlResponse;
import study.multiproject.shortenurl.dao.ShortenUrlRepository;
import study.multiproject.shortenurl.domain.ShortenUrl;
import study.multiproject.shortenurl.exception.LackOfShortenUrlKeyException;
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
        String shortenUrlKey = getUniqueShortenUrlKey();

        ShortenUrl shortenUrl = shortenUrlRepository.save(ShortenUrl.builder()
                                                        .originalUrl(originalUrl)
                                                        .shortenUrlKey(shortenUrlKey)
                                                        .build());
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

    /**
     * 단축 URL 키를 생성한다.
     */
    private String getUniqueShortenUrlKey() {
        int count = 0;
        while (count++ < 5) {
            String shortenUrlKey = ShortenUrl.generateShortenUrlKey();
            if (shortenUrlRepository.findByShortenUrlKey(shortenUrlKey).isEmpty()) {
                return shortenUrlKey;
            }
        }
        throw new LackOfShortenUrlKeyException();
    }
}
