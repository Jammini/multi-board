package study.multiproject.api.service.post.response;

import study.multiproject.domain.post.Post;

public record PostResponse(Long id, String title, long viewCount, String content) {

    public PostResponse(Post post) {
        this(post.getId(), post.getTitle(), post.getViewCount(), post.getContent());
    }
}
