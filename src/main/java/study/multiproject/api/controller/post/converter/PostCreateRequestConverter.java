package study.multiproject.api.controller.post.converter;

import org.springframework.stereotype.Component;
import study.multiproject.api.controller.post.request.PostCreateRequest;
import study.multiproject.api.service.post.request.PostCreateServiceRequest;

@Component
public class PostCreateRequestConverter {

    public PostCreateServiceRequest toServiceRequest(PostCreateRequest request) {
        return new PostCreateServiceRequest(request.title(), request.content(), request.hashtags(),
            request.fileIds());
    }
}
