package study.multiproject.post.application;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.post.dao.PostRepository;

@Service
@RequiredArgsConstructor
public class LocalBufferFlusher {

    private final LocalViewBuffer buffer;
    private final PostRepository postRepository;

    /**
     * 로컬 버퍼에 저장된 게시글 조회수가 있다면 데이터베이스에 반영한다.
     */
    @Scheduled(fixedRate = 60_000)  // 1분마다 실행
    @Transactional
    public void flushToDb() {
        Map<Long, Long> deltaMap = buffer.drain();
        if (deltaMap.isEmpty()) {
            return;
        }

        deltaMap.forEach((postId, delta) -> {
            if (delta > 0) {
                postRepository.incrementViewCount(postId, delta); // 게시글 조회수 증가
            }
        });
    }
}
