package study.multiproject.post.application;

import static study.multiproject.global.util.PostCountConstants.extractPostIdFromKey;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.post.dao.LocalPostViewCountRepository;
import study.multiproject.post.dao.PostRepository;

@Service
@RequiredArgsConstructor
public class LocalPostViewCountScheduler {

    private final LocalPostViewCountRepository postViewCountRepository;
    private final PostRepository postRepository;

    /**
     * 1분마다 로컬 캐시에 저장된 조회수를 데이터베이스에 저장
     */
    @Transactional
    @Scheduled(fixedRate = 60_000)
    public void saveViewCountToDatabase() {
        Set<String> keys = postViewCountRepository.getAllKeys();
        for (String key : keys) {
            long postId = extractPostIdFromKey(key);
            Long viewCount = postViewCountRepository.getViewCount(key);
            postRepository.findById(postId).ifPresent(post -> post.addViewCount(viewCount));
            postViewCountRepository.deleteKey(key);
        }
    }
}
