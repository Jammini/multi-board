package study.multiproject.hashtag.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import study.multiproject.hashtag.domain.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);
}
