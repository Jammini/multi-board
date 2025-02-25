package study.multiproject.api.controller.comment.request;

public record CommentCreateRequest(
    Long postId,
    String nickname,
    String content,
    String path,
    Long writerId) {

}
