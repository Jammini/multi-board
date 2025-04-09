package study.multiproject.comment.application.response;

import java.util.List;

public record CommentPageResponse(List<CommentResponse> comment, Long commentCount) {

}
