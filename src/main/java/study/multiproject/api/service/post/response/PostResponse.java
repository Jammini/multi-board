package study.multiproject.api.service.post.response;

import java.util.Set;
import java.util.stream.Collectors;
import study.multiproject.domain.post.Hashtag;
import study.multiproject.domain.post.Post;

public record PostResponse(Long id, String title, long viewCount, String content,
                           Set<String> hashtags) {

    public PostResponse(Post post) {
        this(post.getId(), post.getTitle(), post.getViewCount(), post.getContent(),
            post.getPostHashtags().stream().map(c -> c.getHashtag().getName()).collect(Collectors.toSet()));
    }
}
