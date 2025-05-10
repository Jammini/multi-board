package study.multiproject.shortenurl.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortenUrl {

    /**
     * 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 원본 URL
     */
    private String originalUrl;

    @Builder
    protected ShortenUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
