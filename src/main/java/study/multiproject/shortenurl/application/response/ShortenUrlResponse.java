package study.multiproject.shortenurl.application.response;

import study.multiproject.shortenurl.domain.ShortenUrl;

public record ShortenUrlResponse(String originalUrl) {

    public ShortenUrlResponse(ShortenUrl shortenUrl) {
        this(
            shortenUrl.getOriginalUrl());
    }
}
