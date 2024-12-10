package study.multiproject.api.controller.post.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import study.multiproject.api.service.post.request.PostPageSearchServiceRequest;

@Getter
public class PostPageSearchRequest {

    @Min(value = 1, message = "페이지 번호가 잘못되었습니다.")
    private final int page;
    @Min(value = 1, message = "사이즈 번호가 잘못되었습니다.")
    private final int size;
    private final String keyword;

    @Builder
    public PostPageSearchRequest(int page, int size, String keyword) {
        this.page = page;
        this.size = size;
        this.keyword = keyword == null ? "" : keyword;
    }

    public PostPageSearchServiceRequest toServiceRequest() {
        return PostPageSearchServiceRequest.builder()
                   .pageable(getPageable())
                   .keyword(keyword)
                   .build();
    }

    private Pageable getPageable() {
        return PageRequest.of(page - 1, size, Sort.by("id").descending());
    }
}
