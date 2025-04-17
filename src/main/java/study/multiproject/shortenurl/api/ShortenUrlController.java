package study.multiproject.shortenurl.api;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.global.common.ApiResponse;
import study.multiproject.shortenurl.api.converter.ShortenUrlCreateRequestConverter;
import study.multiproject.shortenurl.api.request.ShortenUrlCreateRequest;
import study.multiproject.shortenurl.application.ShortenUrlService;
import study.multiproject.shortenurl.application.request.ShortenUrlCreateServiceRequest;
import study.multiproject.shortenurl.application.response.ShortenUrlInformationResponse;
import study.multiproject.shortenurl.application.response.ShortenUrlResponse;

@RestController
@RequiredArgsConstructor
public class ShortenUrlController {

    private final ShortenUrlService shortenUrlService;
    private final ShortenUrlCreateRequestConverter shortenUrlCreateRequestConverter;

    /**
     * ShortenUrl 생성
     */
    @PostMapping("/shortenUrl")
    public ApiResponse<?> createShortenUrl(@RequestBody @Valid ShortenUrlCreateRequest request) {
        ShortenUrlCreateServiceRequest serviceRequest = shortenUrlCreateRequestConverter.toServiceRequest(request);
        ShortenUrlResponse response = shortenUrlService.generateShortenUrl(serviceRequest);
        return ApiResponse.success(response);
    }

    /**
     * ShortenUrl 리다이렉트
     */
    @GetMapping("/{shortenUrlKey}")
    public ResponseEntity<?> redirectShortenUrl(@PathVariable String shortenUrlKey) {
        String originalUrl = shortenUrlService.getOriginalUrlByShortenUrlKey(shortenUrlKey);
        URI redirectUri = URI.create(originalUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * ShortenUrl 정보 조회
     */
    @GetMapping("/shortenUrl/{shortenUrlKey}")
    public ApiResponse<ShortenUrlInformationResponse> getShortenUrlInformation(
        @PathVariable String shortenUrlKey) {
        ShortenUrlInformationResponse response = shortenUrlService.getShortenUrlInformationByShortenUrlKey(shortenUrlKey);
        return ApiResponse.success(response);
    }

}

