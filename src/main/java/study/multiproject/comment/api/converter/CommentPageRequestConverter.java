package study.multiproject.comment.api.converter;

import org.springframework.stereotype.Component;
import study.multiproject.comment.api.request.CommentPageRequest;
import study.multiproject.comment.application.request.CommentPageServiceRequest;

@Component
public class CommentPageRequestConverter {

    public CommentPageServiceRequest toServiceRequest(CommentPageRequest request) {
        return new CommentPageServiceRequest(request.getPageable(), request.postId());
    }

}
