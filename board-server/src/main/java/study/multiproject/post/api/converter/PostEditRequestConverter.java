package study.multiproject.post.api.converter;

import org.springframework.stereotype.Component;
import study.multiproject.post.api.request.PostEditRequest;
import study.multiproject.post.application.request.PostEditServiceRequest;

@Component
public class PostEditRequestConverter {

    public PostEditServiceRequest toServiceRequest(PostEditRequest request, Long userId) {
        return new PostEditServiceRequest(request.title(), request.content(), request.isSecret(), request.hashtags(), request.fileIds(), userId);
    }
}
