package study.multiproject.api.service.post.request;

import java.util.List;
import java.util.Objects;
import study.multiproject.domain.post.Post;
import study.multiproject.domain.user.User;

public record PostCreateServiceRequest(String title, String content, List<String> hashtags,
                                       List<Long> fileIds, Long userId) {

    public PostCreateServiceRequest {
        hashtags = Objects.requireNonNullElse(hashtags, List.of());
        fileIds = Objects.requireNonNullElse(fileIds, List.of());
    }

    public Post toEntity(User user) {
        return Post.builder()
                   .title(title)
                   .content(content)
                   .user(user)
                   .build();
    }
}
