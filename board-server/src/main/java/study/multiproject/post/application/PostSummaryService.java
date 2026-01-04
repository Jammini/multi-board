package study.multiproject.post.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.post.dao.PostRepository;
import study.multiproject.post.domain.Post;
import study.multiproject.post.exception.PostNotFoundException;
import study.multiproject.post.infra.ai.AiSummaryClient;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostSummaryService {

    private final PostRepository postRepository;
    private final AiSummaryClient aiSummaryClient;

    /**
     * 게시글을 AI로 요약
     * 이미 저장된 요약이 있으면 재사용하고, 없으면 새로 생성하여 DB에 저장
     */
    @Transactional
    public String summarizePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        // 이미 저장된 요약이 있으면 반환
        if (post.getSummary() != null && !post.getSummary().isEmpty()) {
            return post.getSummary();
        }

        // 새로운 요약 생성
        String contentToSummarize = "제목: " + post.getTitle() + "\n\n" + post.getContent();
        String summary = aiSummaryClient.summarize(contentToSummarize);

        // DB에 저장
        post.updateSummary(summary);
        return summary;
    }
}
