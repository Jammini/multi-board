package study.multiproject.post.dao;

import java.util.Set;

public interface PostViewCountRepository {

    /**
     * 조회수 증가
     */
    Long increaseData(long postId, long userId);

    /**
     * 모든 조회수 키 조회
     */
    Set<String> getAllKeys();

    /**
     * 조회수 조회
     */
    Long getViewCount(String key);

    /**
     * 키 삭제
     */
    void deleteKey(String key);
}
