package study.multiproject.api.controller.post.converter;

import org.springframework.stereotype.Component;
import study.multiproject.api.controller.post.request.PostEditRequest;
import study.multiproject.api.service.post.request.PostEditServiceRequest;

@Component
public class PostEditRequestConverter {

    public PostEditServiceRequest toServiceRequest(PostEditRequest request) {
        return new PostEditServiceRequest(request.title(), request.content(), request.hashtags());
    }
}
