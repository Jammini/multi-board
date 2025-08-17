package study.multiproject.post.application;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.post.exception.PostNotFoundException;
import study.multiproject.post.domain.Post;
import study.multiproject.post.dao.PostRepository;

@Service
@RequiredArgsConstructor
public class PostHitsScheduler {

    private final PostHitsService postHitsService;
    private final PostRepository postRepository;

    /**
     * 1분마다 Redis에 저장된 조회수를 데이터베이스에 저장
     */
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void saveHitsToDatabase() {
        Set<String> keys = postHitsService.getAllKeys();
        for (String key : keys) {
            long postId = extractPostIdFromKey(key);
            Integer hits = postHitsService.getHits(key);
            if (hits != null) {
                try {
                    Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
                    post.changeViewCount(hits);
                } catch (PostNotFoundException e) {
                    // 게시글이 삭제된 경우 redis 값 삭제
                    postHitsService.deleteKey(key);
                }
            }
        }
    }

    /**
     * Redis에 저장된 조회수 키에서 게시글 ID 추출
     */
    private long extractPostIdFromKey(String key) {
        String[] parts = key.split("_");
        return Long.parseLong(parts[2]);
    }
}
