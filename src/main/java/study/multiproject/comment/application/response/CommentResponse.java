package study.multiproject.comment.application.response;

import java.time.LocalDateTime;
import study.multiproject.comment.domain.Comment;

public record CommentResponse(Long id, String nickname, String comment, String path,
                              Long postId, Long writerId, String writerName, boolean deleted,
                              LocalDateTime createdAt) {

    public CommentResponse(Comment comment) {
        this(comment.getId(), comment.getNickname(), comment.getContent(),
            comment.getCommentPath().getPath(), comment.getPostId(), comment.getUser().getId(), comment.getUser().getName(),
            comment.isDeleted(), comment.getCreatedAt());
    }
}
