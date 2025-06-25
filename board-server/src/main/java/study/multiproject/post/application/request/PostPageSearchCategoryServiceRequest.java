package study.multiproject.post.application.request;

import org.springframework.data.domain.Pageable;

public record PostPageSearchCategoryServiceRequest(Pageable pageable, String keyword, Long categoryId, Long userId) {
}
