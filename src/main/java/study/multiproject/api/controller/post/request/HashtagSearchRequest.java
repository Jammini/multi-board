package study.multiproject.api.controller.post.request;

import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record HashtagSearchRequest(
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.") int page,
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.") int size,
    String keyword
) {

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size, Sort.by("id").descending());
    }
}
