package study.multiproject.like.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    /**
     * 좋아요 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 게시물 식별자
     */
    private Long postId;

    /**
     * 사용자 식별자
     */
    private Long userId;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    @Builder
    public PostLike(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}
