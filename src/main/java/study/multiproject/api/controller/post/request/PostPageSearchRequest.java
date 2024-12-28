package study.multiproject.api.controller.post.request;

import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import study.multiproject.api.service.post.request.PostPageSearchServiceRequest;

public record PostPageSearchRequest(
    @Min(value = 1, message = "페이지 번호가 잘못되었습니다.") int page,
    @Min(value = 1, message = "사이즈 번호가 잘못되었습니다.") int size,
    String keyword
) {

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size, Sort.by("id").descending());
    }
}
