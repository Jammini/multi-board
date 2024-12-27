package study.multiproject.api.service.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.api.service.post.response.HashtagResponse;
import study.multiproject.domain.post.HashtagRepository;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public List<HashtagResponse> getAllHashtags() {
        return hashtagRepository.findAll().stream()
                   .map(tag -> new HashtagResponse(tag.getName()))
                   .toList();
    }
}
