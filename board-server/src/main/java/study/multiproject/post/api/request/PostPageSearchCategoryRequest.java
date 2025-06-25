package study.multiproject.post.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PostPageSearchCategoryRequest(
    @Min(value = 1, message = "페이지 번호가 잘못되었습니다.") int page,
    @Min(value = 1, message = "사이즈 번호가 잘못되었습니다.") int size,
    String keyword,
    @NotNull(message = "카테고리 ID는 필수입니다.")
    Long categoryId
) {

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size, Sort.by("id").descending());
    }
}
