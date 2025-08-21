package study.multiproject.post.application;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import org.springframework.stereotype.Component;

@Component
public class LocalViewBuffer {
    private final ConcurrentHashMap<Long, LongAdder> map = new ConcurrentHashMap<>();

    /**
     * 로컬 버퍼에 게시글 조회수 증가
     */
    public void increase(long postId) {
        map.computeIfAbsent(postId, k -> new LongAdder()).increment();
    }

    /**
     * 로컬 버퍼에 저장된 게시글 조회수 스냅샷을 반환하고 초기화
     */
    public Map<Long, Long> drain() {
        Map<Long, Long> snap = new HashMap<>();
        map.forEach((k, adder) -> snap.put(k, adder.sumThenReset()));
        return snap;
    }
}
