package study.multiproject.api.controller.post.converter;

import org.springframework.stereotype.Component;
import study.multiproject.api.controller.post.request.HashtagSearchRequest;
import study.multiproject.api.service.post.request.HashtagSearchServiceRequest;

@Component
public class HashtagSearchRequestConverter {

    public HashtagSearchServiceRequest toServiceRequest(HashtagSearchRequest request) {
        return new HashtagSearchServiceRequest(request.tagName(), request.getPageable());
    }
}
