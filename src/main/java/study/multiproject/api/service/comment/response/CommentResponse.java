package study.multiproject.api.service.comment.response;

import java.time.LocalDateTime;
import study.multiproject.domain.comment.Comment;

public record CommentResponse(Long id, String nickname, String comment, Long parentCommentId,
                              Long postId, Long writerId, boolean deleted,
                              LocalDateTime createdAt) {

    public CommentResponse(Comment comment) {
        this(comment.getId(), comment.getNickname(), comment.getContent(),
            comment.getParentCommentId(), comment.getPostId(), comment.getWriterId(),
            comment.isDeleted(), comment.getCreatedAt());
    }
}
