package study.multiproject.shortenurl.common;

public interface Base62 {
    String encode(long number);
    long decode(String base62);
}
