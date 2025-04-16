package study.multiproject.hashtag.application.request;

import org.springframework.data.domain.Pageable;

public record HashtagSearchServiceRequest(String keyword, Pageable pageable, Long userId) {
}
