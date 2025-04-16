package study.multiproject.post.application.response;

import static study.multiproject.global.util.TimeParsingUtils.formatterString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import study.multiproject.file.application.response.FileResponse;
import study.multiproject.post.domain.Post;

public record PostResponse(Long id, String title, long viewCount, String content, boolean isSecret,
                           boolean isOwner, Long authorId, String authorName, Set<String> hashtags,
                           List<FileResponse> files, String createdAt) {

    public PostResponse(Post post, Long userId) {
        this(post.getId(), post.getTitle(), post.getViewCount(), post.getContent(), post.isSecret(),
            (post.getUser().getId().equals(userId)), post.getUser().getId(), post.getUser().getName(),
            post.getPostHashtags().stream().map(c -> c.getHashtag().getName())
                .collect(Collectors.toSet()),
            post.getUploadFiles().stream().map(FileResponse::new).toList(), formatterString(post.getCreatedAt()));
    }

    public PostResponse(Post post) {
        this(post.getId(), post.getTitle(), post.getViewCount(), post.getContent(), post.isSecret(),
            false, post.getUser().getId(), post.getUser().getName(),
            post.getPostHashtags().stream().map(c -> c.getHashtag().getName())
                .collect(Collectors.toSet()),
            post.getUploadFiles().stream().map(FileResponse::new).toList(), formatterString(post.getCreatedAt()));
    }
}
