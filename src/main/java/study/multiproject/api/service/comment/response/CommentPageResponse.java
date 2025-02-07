package study.multiproject.api.service.comment.response;

import java.util.List;

public record CommentPageResponse(List<CommentResponse> comment, Long commentCount) {

}
