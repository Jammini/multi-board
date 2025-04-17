package study.multiproject.shortenurl.api.request;

import jakarta.validation.constraints.NotNull;

public record ShortenUrlCreateRequest(

    @NotNull(message = "URL은 필수입니다.")
    String originalUrl
) {

}
