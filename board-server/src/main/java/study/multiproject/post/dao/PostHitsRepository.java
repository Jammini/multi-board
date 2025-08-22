package study.multiproject.post.dao;

import java.util.Set;

public interface PostHitsRepository {

    /**
     * 조회수 증가
     */
    void increaseData(String visitorId);

    /**
     * 조회수 저장
     */
    void addToSet(String visitorId, Long postId);

    /**
     * 조회수 존재 여부 확인
     */
    boolean isExistInSet(String visitorId, Long postId);

    /**
     * 모든 조회수 키 조회
     */
    Set<String> getAllKeys();

    /**
     * 조회수 조회
     */
    Integer getHits(String key);

    /**
     * 키 삭제
     */
    void deleteKey(String key);
}
