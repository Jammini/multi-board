package study.multiproject.post.application.request;

import java.util.List;
import java.util.Objects;
import study.multiproject.post.domain.Post;
import study.multiproject.user.domain.User;

public record PostCreateServiceRequest(
    String title,
    String content,
    boolean isSecret,
    List<String> hashtags,
    List<Long> fileIds,
    Long categoryId,
    Long userId
) {
    public PostCreateServiceRequest {
        hashtags = Objects.requireNonNullElse(hashtags, List.of());
        fileIds = Objects.requireNonNullElse(fileIds, List.of());
    }

    public Post toEntity(User user, Long categoryId, String categoryName) {
        return Post.builder()
                   .title(title)
                   .content(content)
                   .isSecret(isSecret)
                   .categoryId(categoryId)
                   .categoryName(categoryName)
                   .user(user)
                   .build();
    }
}
