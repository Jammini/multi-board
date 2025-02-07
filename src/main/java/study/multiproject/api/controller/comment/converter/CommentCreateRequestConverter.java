package study.multiproject.api.controller.comment.converter;

import org.springframework.stereotype.Component;
import study.multiproject.api.controller.comment.request.CommentCreateRequest;
import study.multiproject.api.service.comment.request.CommentCreateServiceRequest;

@Component
public class CommentCreateRequestConverter {

    public CommentCreateServiceRequest toServiceRequest(CommentCreateRequest request) {
        return new CommentCreateServiceRequest(request.postId(), request.nickname(),
            request.content(), request.parentCommentId(), request.writerId());
    }

}
