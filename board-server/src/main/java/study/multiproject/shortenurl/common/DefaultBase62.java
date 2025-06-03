package study.multiproject.shortenurl.common;

import org.springframework.stereotype.Component;

@Component
public class DefaultBase62 implements Base62 {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = BASE62.length();

    @Override
    public String encode(long num) {
        StringBuilder encoded = new StringBuilder();
        while (num > 0) {
            encoded.append(BASE62.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return encoded.reverse().toString();
    }

    @Override
    public long decode(String base62) {
        long decoded = 0;
        for (int i = 0; i < base62.length(); i++) {
            decoded = decoded * BASE + BASE62.indexOf(base62.charAt(i));
        }
        return decoded;
    }
}
