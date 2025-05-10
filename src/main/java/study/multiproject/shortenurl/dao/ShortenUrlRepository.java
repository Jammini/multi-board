package study.multiproject.shortenurl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import study.multiproject.shortenurl.domain.ShortenUrl;

public interface ShortenUrlRepository extends JpaRepository<ShortenUrl, Long> {

}
