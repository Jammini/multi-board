package study.multiproject.global.util;

public class PostCountConstants {
    public static final String POST_VIEW_COUNT_KEY = "post:viewCount:";
    public static final String LOCAL_CACHE_MANAGER = "localCacheManager";
    public static final String REDIS_CACHE_MANAGER = "redisCacheManager";
    public static final String VIEW_DEDUPE_LOCAL = "viewDedupeLocal";
    public static final String VIEW_DEDUPE = "viewDedupe";

    private PostCountConstants() {
    }

    /**
     * Redis에 저장된 조회수 키에서 게시글 ID 추출
     */
    public static long extractPostIdFromKey(String key) {
        String[] parts = key.split(":");
        return Long.parseLong(parts[2]);
    }
}
