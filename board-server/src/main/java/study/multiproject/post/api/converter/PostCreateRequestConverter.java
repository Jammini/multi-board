package study.multiproject.post.api.converter;

import org.springframework.stereotype.Component;
import study.multiproject.post.api.request.PostCreateRequest;
import study.multiproject.post.application.request.PostCreateServiceRequest;

@Component
public class PostCreateRequestConverter {

    public PostCreateServiceRequest toServiceRequest(PostCreateRequest request, Long userId) {
        return new PostCreateServiceRequest(
            request.title(),
            request.content(),
            request.isSecret(),
            request.hashtags(),
            request.fileIds(),
            request.categoryId(),
            userId
        );
    }
}
