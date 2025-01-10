package study.multiproject.api.service.post.request;

import java.util.List;
import java.util.Objects;
import study.multiproject.api.service.file.request.FileData;
import study.multiproject.domain.post.Post;

public record PostCreateServiceRequest(String title, String content, List<String> hashtags,
                                       List<FileData> files) {

    public PostCreateServiceRequest {
        hashtags = Objects.requireNonNullElse(hashtags, List.of());
        files = Objects.requireNonNullElse(files, List.of());
    }

    public Post toEntity() {
        return Post.builder()
                   .title(title)
                   .content(content)
                   .build();
    }
}
