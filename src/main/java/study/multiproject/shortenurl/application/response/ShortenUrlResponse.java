package study.multiproject.shortenurl.application.response;

import study.multiproject.shortenurl.domain.ShortenUrl;

public record ShortenUrlResponse(String originalUrl, String shortenUrlKey) {

    public ShortenUrlResponse(ShortenUrl shortenUrl) {
        this(
            shortenUrl.getOriginalUrl(),
            shortenUrl.getShortenUrlKey());
    }
}
