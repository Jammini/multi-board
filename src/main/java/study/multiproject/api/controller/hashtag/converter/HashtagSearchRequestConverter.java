package study.multiproject.api.controller.hashtag.converter;

import org.springframework.stereotype.Component;
import study.multiproject.api.controller.hashtag.request.HashtagSearchRequest;
import study.multiproject.api.service.hashtag.request.HashtagSearchServiceRequest;

@Component
public class HashtagSearchRequestConverter {

    public HashtagSearchServiceRequest toServiceRequest(HashtagSearchRequest request) {
        return new HashtagSearchServiceRequest(request.keyword(), request.getPageable());
    }
}
