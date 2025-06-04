package study.multiproject.post.application.response;

import java.util.List;
import org.springframework.data.domain.Page;
import study.multiproject.post.domain.Post;

public record PagingResponse(long page, long size, long totalCount, List<PostResponse> items) {

    public PagingResponse(Page<Post> page, Long userId) {
        this(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.map(post -> new PostResponse(post, userId)).toList());
    }
}
