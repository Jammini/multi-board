package study.multiproject.api.service.comment.request;

import study.multiproject.domain.comment.Comment;
import study.multiproject.domain.comment.CommentPath;
import study.multiproject.domain.user.User;

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
