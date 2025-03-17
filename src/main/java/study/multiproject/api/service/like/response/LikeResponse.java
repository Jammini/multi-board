package study.multiproject.api.service.like.response;

import java.time.LocalDateTime;
import study.multiproject.domain.like.PostLike;

public record LikeResponse(Long id, Long postId, Long userId, LocalDateTime createdAt) {

    public LikeResponse(PostLike postLike) {
        this(postLike.getId(), postLike.getPostId(), postLike.getUserId(), postLike.getCreatedAt());
    }
}
