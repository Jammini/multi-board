package study.multiproject.post.application.request;

import java.util.List;
import java.util.Objects;

public record PostEditServiceRequest(String title, String content, boolean isSecret, List<String> hashtags, List<Long> fileIds, Long userId) {

    public PostEditServiceRequest {
        hashtags = Objects.requireNonNullElse(hashtags, List.of());
        fileIds = Objects.requireNonNullElse(fileIds, List.of());
    }
}
