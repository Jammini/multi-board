package study.multiproject.api.controller.post;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.api.common.ApiResponse;
import study.multiproject.api.controller.post.converter.HashtagSearchRequestConverter;
import study.multiproject.api.controller.post.request.HashtagSearchRequest;
import study.multiproject.api.service.post.HashtagService;
import study.multiproject.api.service.post.PostService;
import study.multiproject.api.service.post.response.HashtagResponse;
import study.multiproject.api.service.post.response.PagingResponse;

@RestController
@RequiredArgsConstructor
public class HashtagController {

    private final PostService postService;
    private final HashtagService hashtagService;
    private final HashtagSearchRequestConverter converter;

    @GetMapping("/hashtags")
    public ApiResponse<List<HashtagResponse>> getAllHashtags() {
        return ApiResponse.success(hashtagService.getAllHashtags());
    }

    @GetMapping("/posts/hashtags/{tagName}")
    public ApiResponse<PagingResponse> getPostsByHashtag(@Valid @ModelAttribute HashtagSearchRequest request) {
        return ApiResponse.success(postService.getPostsByHashtag(converter.toServiceRequest(request)));
    }
}
