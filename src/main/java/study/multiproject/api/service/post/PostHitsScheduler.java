package study.multiproject.api.service.post;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import study.multiproject.api.service.post.exception.PostNotFoundException;
import study.multiproject.domain.post.Post;
import study.multiproject.domain.post.PostRepository;

@Service
@RequiredArgsConstructor
public class PostHitsScheduler {

    private final PostHitsService postHitsService;
    private final PostRepository postRepository;

    /**
     * 1분마다 Redis에 저장된 조회수를 데이터베이스에 저장
     */
    @Scheduled(fixedRate = 60000)
    public void saveHitsToDatabase() {
        Set<String> keys = postHitsService.getAllKeys();
        for (String key : keys) {
            Long postId = extractPostIdFromKey(key);
            Integer hits = postHitsService.getHits(key);
            if (hits != null) {
                Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
                post.incrementViewCount(hits);
                postRepository.save(post);
            }
        }
    }

    /**
     * Redis에 저장된 조회수 키에서 게시글 ID 추출
     */
    private Long extractPostIdFromKey(String key) {
        String[] parts = key.split("_");
        return Long.valueOf(parts[2]);
    }
}
