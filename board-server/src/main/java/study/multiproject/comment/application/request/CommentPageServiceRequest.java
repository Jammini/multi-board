package study.multiproject.comment.application.request;

import org.springframework.data.domain.Pageable;

public record CommentPageServiceRequest(
    Pageable pageable,
    Long postId) {

}
