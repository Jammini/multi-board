package study.multiproject.api.controller.post;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 모든 해시태그 조회
     */
    @GetMapping("/hashtags")
    public ApiResponse<List<HashtagResponse>> getAllHashtags() {
        return ApiResponse.success(hashtagService.getAllHashtags());
    }

    /**
     * 해시태그를 이용하여 게시글 조회
     */
    @GetMapping("/posts/hashtags")
    public ApiResponse<PagingResponse> getPostsByHashtag(@Valid HashtagSearchRequest request) {
        return ApiResponse.success(
            postService.getPostsByHashtag(converter.toServiceRequest(request)));
    }
}
