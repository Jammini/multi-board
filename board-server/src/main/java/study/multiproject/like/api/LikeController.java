package study.multiproject.like.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.like.application.LikeService;
import study.multiproject.like.application.response.LikeResponse;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요 조회
     */
    @GetMapping("/likes/posts/{postId}/users/{userId}")
    public LikeResponse read(@PathVariable Long postId, @PathVariable Long userId) {
        return likeService.read(postId, userId);
    }

    /**
     * 좋아요 개수 조회
     */
    @GetMapping("/likes/posts/{postId}/count")
    public Long count(@PathVariable Long postId) {
        return likeService.count(postId);
    }

    /**
     * 좋아요 갱신 1
     */
    @PostMapping("/likes/posts/{postId}/users/{userId}/pessimistic-lock-1")
    public void likePessimisticLock1(@PathVariable Long postId, @PathVariable Long userId) {
        likeService.likePessimisticLock1(postId, userId);
    }

    /**
     * 좋아요 해제 1
     */
    @DeleteMapping("/likes/posts/{postId}/users/{userId}/pessimistic-lock-1")
    public void unlikePessimisticLock1(@PathVariable Long postId, @PathVariable Long userId) {
        likeService.unlikePessimisticLock1(postId, userId);
    }

    /**
     * 좋아요 갱신 2
     */
    @PostMapping("/likes/posts/{postId}/users/{userId}/pessimistic-lock-2")
    public void likePessimisticLock2(@PathVariable Long postId, @PathVariable Long userId) {
        likeService.likePessimisticLock2(postId, userId);
    }

    /**
     * 좋아요 해제 2
     */
    @DeleteMapping("/likes/posts/{postId}/users/{userId}/pessimistic-lock-2")
    public void unlikePessimisticLock2(@PathVariable Long postId, @PathVariable Long userId) {
        likeService.unlikePessimisticLock2(postId, userId);
    }

    /**
     * 좋아요 갱신 3
     */
    @PostMapping("/likes/posts/{postId}/users/{userId}/optimistic-lock")
    public void likeOptimisticLock(@PathVariable Long postId, @PathVariable Long userId) {
        likeService.likeOptimisticLock(postId, userId);
    }

    /**
     * 좋아요 해제 3
     */
    @DeleteMapping("/likes/posts/{postId}/users/{userId}/optimistic-lock")
    public void unlikeOptimisticLock(@PathVariable Long postId, @PathVariable Long userId) {
        likeService.unlikeOptimisticLock(postId, userId);
    }
}
