package study.multiproject.api.service.post.response;

import java.util.List;
import org.springframework.data.domain.Page;
import study.multiproject.domain.post.Post;

public record PagingResponse(long page, long size, long totalCount, List<PostResponse> items) {

    public PagingResponse(Page<Post> page) {
        this(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.map(PostResponse::new).toList());
    }
}
