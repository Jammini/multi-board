package study.multiproject.shortenurl.api.converter;

import org.springframework.stereotype.Component;
import study.multiproject.shortenurl.api.request.ShortenUrlCreateRequest;
import study.multiproject.shortenurl.application.request.ShortenUrlCreateServiceRequest;

@Component
public class ShortenUrlCreateRequestConverter {

    public ShortenUrlCreateServiceRequest toServiceRequest(ShortenUrlCreateRequest request) {
        return new ShortenUrlCreateServiceRequest(
            request.originalUrl()
        );
    }
}
