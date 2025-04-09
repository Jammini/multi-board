package study.multiproject.like.dao;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.multiproject.like.domain.LikeCount;

@Repository
public interface LikeCountRepository extends JpaRepository<LikeCount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LikeCount> findLockedByPostId(Long postId);

    @Modifying
    @Query("UPDATE LikeCount lc SET lc.count = lc.count + 1 WHERE lc.postId = :postId")
    int increase(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE LikeCount lc SET lc.count = lc.count - 1 WHERE lc.postId = :postId")
    int decrease(@Param("postId") Long postId);
}
