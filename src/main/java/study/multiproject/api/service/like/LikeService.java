package study.multiproject.api.service.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.api.service.like.response.LikeResponse;
import study.multiproject.domain.like.PostLike;
import study.multiproject.domain.like.LikeCount;
import study.multiproject.domain.like.LikeCountRepository;
import study.multiproject.domain.like.PostLikeRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final LikeCountRepository likeCountRepository;

    /**
     * 좋아요 조회
     */
    public LikeResponse read(Long postId, Long userId) {
        return postLikeRepository.findByPostIdAndUserId(postId, userId)
                   .map(LikeResponse::new)
                   .orElseThrow();
    }

    /**
     * 좋아요 PessimisticLock1 방식으로 갱신
     */
    @Transactional
    public void likePessimisticLock1(Long postId, Long userId) {
        postLikeRepository.save(PostLike.builder()
                                .postId(postId)
                                .userId(userId)
                                .build());

        int result = likeCountRepository.increase(postId);
        if (result == 0) {
            likeCountRepository.save(LikeCount.init(postId, 1L));
        }
    }

    /**
     * 좋아요 PessimisticLock1 방식으로 해제
     */
    @Transactional
    public void unlikePessimisticLock1(Long postId, Long userId) {
        postLikeRepository.findByPostIdAndUserId(postId, userId)
            .ifPresent(postLike -> {
                postLikeRepository.delete(postLike);
                likeCountRepository.decrease(postId);
            });
    }

    /**
     * 좋아요 PessimisticLock2 방식으로 갱신
     */
    @Transactional
    public void likePessimisticLock2(Long postId, Long userId) {
        postLikeRepository.save(PostLike.builder()
                                .postId(postId)
                                .userId(userId)
                                .build());
        likeCountRepository.findLockedByPostId(postId)
            .ifPresentOrElse(
                LikeCount::increase,
                () -> likeCountRepository.save(LikeCount.init(postId, 1L)));
    }

    /**
     * 좋아요 PessimisticLock2 방식으로 해제
     */
    @Transactional
    public void unlikePessimisticLock2(Long postId, Long userId) {
        postLikeRepository.findByPostIdAndUserId(postId, userId)
            .ifPresent(postLike -> {
                postLikeRepository.delete(postLike);
                likeCountRepository.findLockedByPostId(postId)
                    .ifPresent(LikeCount::decrease);
            });
    }

    /**
     * 좋아요 OptimisticLock 방식으로 갱신
     */
    @Transactional
    public void likeOptimisticLock(Long postId, Long userId) {
        postLikeRepository.save(PostLike.builder()
                                .postId(postId)
                                .userId(userId)
                                .build());

        likeCountRepository.findById(postId).ifPresentOrElse(
            LikeCount::increase,
            () -> likeCountRepository.save(LikeCount.init(postId, 1L)));
    }

    /**
     * 좋아요 OptimisticLock 방식으로 해제
     */
    @Transactional
    public void unlikeOptimisticLock(Long postId, Long userId) {
        postLikeRepository.findByPostIdAndUserId(postId, userId)
            .ifPresent(postLike -> {
                postLikeRepository.delete(postLike);
                likeCountRepository.findById(postId)
                    .ifPresent(LikeCount::decrease);
            });
    }

    public Long count(Long postId) {
        return likeCountRepository.findById(postId)
                   .map(LikeCount::getCount)
                   .orElse(0L);
    }
}
