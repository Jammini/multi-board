package study.multiproject.post.api.converter;

import org.springframework.stereotype.Component;
import study.multiproject.post.api.request.PostPageSearchRequest;
import study.multiproject.post.application.request.PostPageSearchServiceRequest;

@Component
public class PostPageSearchRequestConverter {

    public PostPageSearchServiceRequest toServiceRequest(PostPageSearchRequest request, Long userId) {
        return new PostPageSearchServiceRequest(request.getPageable(), request.keyword(), userId);
    }
}
