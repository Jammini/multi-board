package study.multiproject.like.application.response;

import java.time.LocalDateTime;
import study.multiproject.like.domain.PostLike;

public record LikeResponse(Long id, Long postId, Long userId, LocalDateTime createdAt) {

    public LikeResponse(PostLike postLike) {
        this(postLike.getId(), postLike.getPostId(), postLike.getUserId(), postLike.getCreatedAt());
    }
}
