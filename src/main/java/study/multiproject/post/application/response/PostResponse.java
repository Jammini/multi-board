package study.multiproject.post.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import study.multiproject.file.application.response.FileResponse;
import study.multiproject.post.domain.Post;

public record PostResponse(
    Long id, String title,
    long viewCount,
    String content,
    boolean isSecret,
    boolean isOwner,
    Long authorId,
    String authorName,
    Set<String> hashtags,
    List<FileResponse> files,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt) {

    public PostResponse(Post post, Long userId) {
        this(
            post.getId(),
            post.getTitle(),
            post.getViewCount(),
            post.getContent(),
            post.isSecret(),
            (post.getUser().getId().equals(userId)),
            post.getUser().getId(),
            post.getUser().getName(),
            post.getPostHashtags().stream().map(c -> c.getHashtag().getName()).collect(Collectors.toSet()),
            post.getUploadFiles().stream().map(FileResponse::new).toList(), post.getCreatedAt());
    }

    public PostResponse(Post post) {
        this(
            post.getId(),
            post.getTitle(),
            post.getViewCount(),
            post.getContent(),
            post.isSecret(),
            false,
            post.getUser().getId(),
            post.getUser().getName(),
            post.getPostHashtags().stream().map(c -> c.getHashtag().getName()).collect(Collectors.toSet()),
            post.getUploadFiles().stream().map(FileResponse::new).toList(), post.getCreatedAt());
    }
}
