package study.multiproject.like.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeCount {

    @Id
    private Long postId;
    private Long count;
    @Version
    private Long version;

    @Builder
    public LikeCount(Long postId, Long count, Long version) {
        this.postId = postId;
        this.count = count;
        this.version = version;
    }

    public static LikeCount init(Long postId, Long count) {
        return LikeCount.builder()
                   .postId(postId)
                   .count(count)
                   .version(0L)
                   .build();
    }

    public void increase() {
        this.count++;
    }

    public void decrease() {
        this.count--;
    }
}
