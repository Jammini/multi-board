package study.multiproject.api.service.comment.request;

import org.springframework.data.domain.Pageable;

public record CommentPageServiceRequest(
    Pageable pageable,
    Long postId) {

}
