package study.multiproject.api.service.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.api.service.post.exception.PostNotFoundException;
import study.multiproject.api.service.post.request.PostCreateServiceRequest;
import study.multiproject.api.service.post.request.PostEditServiceRequest;
import study.multiproject.api.service.post.request.PostPageSearchServiceRequest;
import study.multiproject.api.service.post.response.PagingResponse;
import study.multiproject.api.service.post.response.PostResponse;
import study.multiproject.domain.post.Post;
import study.multiproject.domain.post.PostEditor;
import study.multiproject.domain.post.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long write(PostCreateServiceRequest request) {
        Post post = postRepository.save(request.toEntity());
        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostResponse get(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return new PostResponse(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
                   .map(PostResponse::new)
                   .toList();
    }

    @Transactional(readOnly = true)
    public PagingResponse getPageList(PostPageSearchServiceRequest request) {
        Page<Post> postPage = postRepository.findByTitleContaining(request.keyword(),
            request.pageable());
        return new PagingResponse(postPage);
    }

    @Transactional
    public Long edit(Long id, PostEditServiceRequest request) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
        PostEditor postEditor = editorBuilder
                                    .title(request.title())
                                    .content(request.content())
                                    .build();
        post.edit(postEditor);
        return post.getId();
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
    }
}
