package study.multiproject.shortenurl.application.response;

import study.multiproject.shortenurl.domain.ShortenUrl;

public record ShortenUrlInformationResponse(
    String originalUrl,
    String shortenUrlKey,
    Long redirectCount
) {

    public ShortenUrlInformationResponse(ShortenUrl shortenUrl) {
        this(
            shortenUrl.getOriginalUrl(),
            shortenUrl.getShortenUrlKey(),
            shortenUrl.getRedirectCount()
        );
    }
}
