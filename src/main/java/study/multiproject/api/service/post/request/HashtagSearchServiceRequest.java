package study.multiproject.api.service.post.request;

import org.springframework.data.domain.Pageable;

public record HashtagSearchServiceRequest(String tagName, Pageable pageable) {
}
