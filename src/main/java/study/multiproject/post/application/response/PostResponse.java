package study.multiproject.post.application.response;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import study.multiproject.file.application.response.FileResponse;
import study.multiproject.post.domain.Post;

public record PostResponse(Long id, String title, long viewCount, String content, Long authorId,
                           String authorName, Set<String> hashtags, List<FileResponse> files) {

    public PostResponse(Post post) {
        this(post.getId(), post.getTitle(), post.getViewCount(), post.getContent(),
            post.getUser().getId(), post.getUser().getName(),
            post.getPostHashtags().stream().map(c -> c.getHashtag().getName())
                .collect(Collectors.toSet()),
            post.getUploadFiles().stream().map(FileResponse::new).toList());
    }
}
