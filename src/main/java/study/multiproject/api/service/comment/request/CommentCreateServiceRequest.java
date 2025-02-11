package study.multiproject.api.service.comment.request;

import study.multiproject.domain.comment.Comment;

public record CommentCreateServiceRequest(Long postId, String nickname, String content,
                                          Long parentCommentId, Long writerId) {

    public Comment toEntity(Comment parentComment) {
        return Comment.builder()
                   .postId(postId)
                   .nickname(nickname)
                   .content(content)
                   .parentCommentId(
                       parentComment == null ? null : parentComment.getId())
                   .writerId(writerId)
                   .build();
    }
}
