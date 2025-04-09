package study.multiproject.hashtag.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.hashtag.application.response.HashtagResponse;
import study.multiproject.hashtag.dao.HashtagRepository;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    /**
     * 모든 해시태그 조회
     */
    @Transactional(readOnly = true)
    public List<HashtagResponse> getAllHashtags() {
        return hashtagRepository.findAll().stream()
                   .map(tag -> new HashtagResponse(tag.getName()))
                   .toList();
    }
}
