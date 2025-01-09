package study.multiproject.domain.post;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContaining(String title, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.postHashtags ph WHERE ph.hashtag.name = :keyword")
    Page<Post> findByHashtagName(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.postHashtags ph LEFT JOIN FETCH ph.hashtag LEFT JOIN FETCH p.uploadFiles f WHERE p.id = :postId")
    Optional<Post> findPostWithHashtags(@Param("postId") Long postId);
}
