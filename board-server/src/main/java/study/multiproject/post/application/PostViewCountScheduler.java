package study.multiproject.post.application;

import static study.multiproject.global.util.PostCountConstants.extractPostIdFromKey;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.post.dao.PostRepository;
import study.multiproject.post.dao.RedisPostViewCountRepository;

@Service
@RequiredArgsConstructor
public class PostViewCountScheduler {

    private final RedisPostViewCountRepository postViewCountRepository;
    private final PostRepository postRepository;

    /**
     * 1분마다 Redis에 저장된 조회수를 데이터베이스에 저장
     */
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void saveViewCountToDatabase() {
        Set<String> keys = postViewCountRepository.getAllKeys();
        for (String key : keys) {
            long postId = extractPostIdFromKey(key);
            long viewCount = postViewCountRepository.getViewCount(key);
            postRepository.findById(postId).ifPresent(post -> post.addViewCount(viewCount));
            postViewCountRepository.deleteKey(key);
        }
    }
}
