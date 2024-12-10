package study.multiproject.api.service.post.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class PostPageSearchServiceRequest {

    private final Pageable pageable;
    private final String keyword;

    @Builder
    public PostPageSearchServiceRequest(Pageable pageable, String keyword) {
        this.pageable = pageable;
        this.keyword = keyword;
    }
}
