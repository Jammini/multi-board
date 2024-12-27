package study.multiproject.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContaining(String title, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.postHashtags ph WHERE ph.hashtag.name = :tagName")
    Page<Post> findByHashtagName(@Param("tagName") String tagName, Pageable pageable);
}
