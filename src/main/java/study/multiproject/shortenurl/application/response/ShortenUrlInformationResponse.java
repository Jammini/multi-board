package study.multiproject.shortenurl.application.response;

import study.multiproject.shortenurl.domain.ShortenUrl;

public record ShortenUrlInformationResponse(
    String originalUrl
) {

    public ShortenUrlInformationResponse(ShortenUrl shortenUrl) {
        this(
            shortenUrl.getOriginalUrl()
        );
    }
}
