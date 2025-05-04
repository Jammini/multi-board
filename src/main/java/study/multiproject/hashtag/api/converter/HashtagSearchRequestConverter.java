package study.multiproject.hashtag.api.converter;

import org.springframework.stereotype.Component;
import study.multiproject.hashtag.api.request.HashtagSearchRequest;
import study.multiproject.hashtag.application.request.HashtagSearchServiceRequest;

@Component
public class HashtagSearchRequestConverter {

    public HashtagSearchServiceRequest toServiceRequest(HashtagSearchRequest request, Long userId) {
        return new HashtagSearchServiceRequest(request.keyword(), request.getPageable(), userId);
    }
}
