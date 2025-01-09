package study.multiproject.api.service.hashtag.request;

import org.springframework.data.domain.Pageable;

public record HashtagSearchServiceRequest(String keyword, Pageable pageable) {
}
