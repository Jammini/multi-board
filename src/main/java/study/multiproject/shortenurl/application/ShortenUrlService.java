package study.multiproject.shortenurl.application;

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

    /**
     * ShortenUrl을 생성한다.
     */
    @Transactional
    public ShortenUrlResponse generateShortenUrl(ShortenUrlCreateServiceRequest request) {
        String originalUrl = request.originalUrl();

        ShortenUrl shortenUrl = shortenUrlRepository.save(ShortenUrl.builder()
                                                     .originalUrl(originalUrl)
                                                     .build());
        String encodeKey = base62.encode(shortenUrl.getId());
        return new ShortenUrlResponse(encodeKey);
    }

    /**
     * ShortenUrlKey를 통해 원본 URL을 조회한다.
     */
    @Transactional(readOnly = true)
    public String getOriginalUrlByShortenUrlKey(String shortenUrlKey) {
        Long id = base62.decode(shortenUrlKey);
        ShortenUrl shortenUrl = shortenUrlRepository.findById(id).orElseThrow(NotFoundShortenUrlException::new);
        return shortenUrl.getOriginalUrl();
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
}
