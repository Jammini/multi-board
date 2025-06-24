package study.multiproject.post.api.converter;

import org.springframework.stereotype.Component;
import study.multiproject.post.api.request.PostPageSearchCategoryRequest;
import study.multiproject.post.application.request.PostPageSearchCategoryServiceRequest;

@Component
public class PostPageSearchCategoryRequestConverter {

    public PostPageSearchCategoryServiceRequest toServiceRequest(PostPageSearchCategoryRequest request, Long userId) {
        return new PostPageSearchCategoryServiceRequest(request.getPageable(), request.keyword(), request.categoryId(), userId);
    }
}
