package study.multiproject.comment.api.converter;

import org.springframework.stereotype.Component;
import study.multiproject.comment.api.request.CommentCreateRequest;
import study.multiproject.comment.application.request.CommentCreateServiceRequest;

@Component
public class CommentCreateRequestConverter {

    public CommentCreateServiceRequest toServiceRequest(CommentCreateRequest request, Long userId) {
        return new CommentCreateServiceRequest(request.postId(), request.nickname(),
            request.content(), request.path(), userId);
    }

}
