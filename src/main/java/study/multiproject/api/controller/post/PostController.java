package study.multiproject.api.controller.post;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.api.common.ApiResponse;
import study.multiproject.api.controller.post.converter.PostCreateRequestConverter;
import study.multiproject.api.controller.post.converter.PostEditRequestConverter;
import study.multiproject.api.controller.post.converter.PostPageSearchRequestConverter;
import study.multiproject.api.controller.post.request.PostCreateRequest;
import study.multiproject.api.controller.post.request.PostEditRequest;
import study.multiproject.api.controller.post.request.PostPageSearchRequest;
import study.multiproject.api.service.post.PostService;
import study.multiproject.api.service.post.response.PagingResponse;
import study.multiproject.api.service.post.response.PostResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostCreateRequestConverter postCreateRequestConverter;
    private final PostEditRequestConverter postEditRequestConverter;
    private final PostPageSearchRequestConverter postPageSearchRequestConverter;

    @PostMapping("/posts")
    public ApiResponse<Long> post(@RequestBody @Valid PostCreateRequest request) {
        return ApiResponse.success(postService.write(postCreateRequestConverter.toServiceRequest(request)));
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<PostResponse> get(@PathVariable Long postId, HttpServletRequest request) {
        String visitorId = request.getRemoteAddr();
        return ApiResponse.success(postService.get(postId, visitorId));
    }

    @GetMapping("/posts")
    public ApiResponse<PagingResponse> getPageList(@Valid PostPageSearchRequest request) {
        return ApiResponse.success(postService.getPageList(postPageSearchRequestConverter.toServiceRequest(request)));
    }

    @PatchMapping("/posts/{postId}")
    public ApiResponse<Long> edit(@PathVariable Long postId,
        @RequestBody @Valid PostEditRequest request) {
        return ApiResponse.success(postService.edit(postId, postEditRequestConverter.toServiceRequest(request)));
    }

    @DeleteMapping("/posts/{postId}")
    public ApiResponse<Void> delete(@PathVariable Long postId) {
        postService.delete(postId);
        return ApiResponse.success(null);
    }
}
