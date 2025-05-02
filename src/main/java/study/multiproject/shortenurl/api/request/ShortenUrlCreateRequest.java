package study.multiproject.shortenurl.api.request;

import jakarta.validation.constraints.NotBlank;

public record ShortenUrlCreateRequest(

    @NotBlank(message = "URL은 필수입니다.")
    String originalUrl
) {

}
