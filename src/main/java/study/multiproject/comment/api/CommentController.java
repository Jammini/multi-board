package study.multiproject.comment.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.global.common.ApiResponse;
import study.multiproject.comment.api.converter.CommentCreateRequestConverter;
import study.multiproject.comment.api.converter.CommentPageRequestConverter;
import study.multiproject.comment.api.request.CommentCreateRequest;
import study.multiproject.comment.api.request.CommentPageRequest;
import study.multiproject.comment.application.CommentService;
import study.multiproject.comment.application.response.CommentPageResponse;
import study.multiproject.comment.application.response.CommentResponse;
import study.multiproject.global.config.security.UserPrincipal;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentCreateRequestConverter commentCreateRequestConverter;
    private final CommentPageRequestConverter commentPageRequestConverter;

    /**
     * 댓글 생성
     */
    @PostMapping("/comments")
    public ApiResponse<CommentResponse> create(@RequestBody CommentCreateRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(commentService.create(commentCreateRequestConverter.toServiceRequest(request, principal.getUserId())));
    }

    /**
     * 댓글 조회
     */
    @GetMapping("/comments/{commentId}")
    public ApiResponse<CommentResponse> get(@PathVariable Long commentId) {
        return ApiResponse.success(commentService.read(commentId));
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ApiResponse.success(null);
    }

    /**
     * 댓글 목록 조회
     */
    @GetMapping("/comments")
    public ApiResponse<CommentPageResponse> readAll(@Valid CommentPageRequest request) {
        return ApiResponse.success(commentService.readAll(commentPageRequestConverter.toServiceRequest(request)));
    }
}
