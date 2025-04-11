package study.multiproject.comment.application.request;

import study.multiproject.comment.domain.Comment;
import study.multiproject.comment.domain.CommentPath;
import study.multiproject.user.domain.User;

public record CommentCreateServiceRequest(Long postId, String nickname, String content, String path,
                                          Long writerId) {

    public Comment toEntity(CommentPath commentPath, User user) {
        return Comment.builder()
                   .postId(postId)
                   .nickname(nickname)
                   .content(content)
                   .commentPath(commentPath)
                   .user(user)
                   .build();
    }
}
