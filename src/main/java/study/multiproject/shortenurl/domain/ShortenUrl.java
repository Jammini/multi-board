package study.multiproject.shortenurl.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.security.SecureRandom;
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

    /**
     * 단축 URL
     */
    private String shortenUrlKey;

    /**
     * redirect 횟수
     */
    private Long redirectCount;

    @Builder
    protected ShortenUrl(String originalUrl) {
        this.originalUrl = originalUrl;
        this.redirectCount = 0L;
    }

    /**
     * redirect 횟수 증가
     */
    public void increaseRedirectCount() {
        this.redirectCount++;
    }

    public void saveShortenUrlKey(String shortenUrlKey) {
        this.shortenUrlKey = shortenUrlKey;
    }

    public static String generate(long id) {
        int RANDOM_BITS = 16;
        SecureRandom RANDOM = new SecureRandom();

        int randomPart = RANDOM.nextInt(1 << RANDOM_BITS); // 65536 - 1
        long composite = (id << RANDOM_BITS) | randomPart;
        return encodeBase62(composite);
    }

    private static String encodeBase62(long num) {
        char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        int BASE = ALPHABET.length;

        if (num == 0) {
            return String.valueOf(ALPHABET[0]);
        }
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(ALPHABET[(int)(num % BASE)]);
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}
