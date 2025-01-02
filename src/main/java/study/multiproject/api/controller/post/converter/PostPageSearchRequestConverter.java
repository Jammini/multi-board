package study.multiproject.api.controller.post.converter;

import org.springframework.stereotype.Component;
import study.multiproject.api.controller.post.request.PostPageSearchRequest;
import study.multiproject.api.service.post.request.PostPageSearchServiceRequest;

@Component
public class PostPageSearchRequestConverter {

    public PostPageSearchServiceRequest toServiceRequest(PostPageSearchRequest request) {
        return new PostPageSearchServiceRequest(request.getPageable(), request.keyword());
    }
}
