package study.multiproject.comment.api.request;

public record CommentCreateRequest(
    Long postId,
    String nickname,
    String content,
    String path,
    Long writerId) {

}
