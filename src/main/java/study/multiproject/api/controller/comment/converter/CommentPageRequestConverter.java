package study.multiproject.api.controller.comment.converter;

import org.springframework.stereotype.Component;
import study.multiproject.api.controller.comment.request.CommentPageRequest;
import study.multiproject.api.service.comment.request.CommentPageServiceRequest;

@Component
public class CommentPageRequestConverter {

    public CommentPageServiceRequest toServiceRequest(CommentPageRequest request) {
        return new CommentPageServiceRequest(request.getPageable(), request.postId());
    }

}
