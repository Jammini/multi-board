package study.multiproject.api.service.post.request;

import org.springframework.data.domain.Pageable;

public record PostPageSearchServiceRequest(Pageable pageable, String keyword) {
}
