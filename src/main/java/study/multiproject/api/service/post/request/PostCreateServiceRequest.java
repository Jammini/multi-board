package study.multiproject.api.service.post.request;

import java.util.List;
import study.multiproject.domain.post.Post;

public record PostCreateServiceRequest(String title, String content, List<String> hashtags) {

    public Post toEntity() {
        return Post.builder()
                   .title(title)
                   .content(content)
                   .build();
    }
}
