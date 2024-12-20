package study.multiproject.api.controller.post;

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

    @PostMapping("/posts")
    public ApiResponse<Long> post(@RequestBody @Valid PostCreateRequest request) {
        return ApiResponse.success(postService.write(request.toServiceRequest()));
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<PostResponse> get(@PathVariable Long postId) {
        return ApiResponse.success(postService.get(postId));
    }

    @GetMapping("/posts")
    public ApiResponse<PagingResponse> getPageList(@Valid PostPageSearchRequest request) {
        return ApiResponse.success(postService.getPageList(request.toServiceRequest()));
    }

    @PatchMapping("/posts/{postId}")
    public ApiResponse<Long> edit(@PathVariable Long postId,
        @RequestBody @Valid PostEditRequest request) {
        return ApiResponse.success(postService.edit(postId, request.toServiceRequest()));
    }

    @DeleteMapping("/posts/{postId}")
    public ApiResponse<Void> delete(@PathVariable Long postId) {
        postService.delete(postId);
        return ApiResponse.success(null);
    }
}
