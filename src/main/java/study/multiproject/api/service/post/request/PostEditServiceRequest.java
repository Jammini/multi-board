package study.multiproject.api.service.post.request;

import java.util.List;
import java.util.Objects;
import study.multiproject.api.service.file.request.FileData;

public record PostEditServiceRequest(String title, String content, List<String> hashtags,
                                     List<Long> filesToDelete, List<FileData> newFiles) {

    public PostEditServiceRequest {
        hashtags = Objects.requireNonNullElse(hashtags, List.of());
        filesToDelete = Objects.requireNonNullElse(filesToDelete, List.of());
        newFiles = Objects.requireNonNullElse(newFiles, List.of());
    }
}
