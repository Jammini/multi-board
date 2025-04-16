package study.multiproject.post.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.file.application.FileService;
import study.multiproject.file.domain.UploadFile;
import study.multiproject.hashtag.application.request.HashtagSearchServiceRequest;
import study.multiproject.hashtag.dao.HashtagRepository;
import study.multiproject.hashtag.domain.Hashtag;
import study.multiproject.post.application.request.PostCreateServiceRequest;
import study.multiproject.post.application.request.PostEditServiceRequest;
import study.multiproject.post.application.request.PostPageSearchServiceRequest;
import study.multiproject.post.application.response.PagingResponse;
import study.multiproject.post.application.response.PostResponse;
import study.multiproject.post.dao.PostRepository;
import study.multiproject.post.domain.Post;
import study.multiproject.post.domain.PostEditor;
import study.multiproject.post.domain.PostHashtag;
import study.multiproject.post.exception.PostNotFoundException;
import study.multiproject.user.application.UserService;
import study.multiproject.user.domain.User;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHitsService postHitsService;
    private final FileService fileService;
    private final UserService userService;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long write(PostCreateServiceRequest request) {
        User user = userService.getUserById(request.userId());
        Post post = postRepository.save(request.toEntity(user));
        linkFilesToPost(request.fileIds(), post);
        saveHashtag(request.hashtags(), post);
        return post.getId();
    }

    /**
     * 게시글 조회
     */
    @Transactional(readOnly = true)
    public PostResponse get(Long id, String visitorId, Long userId) {
        Post post = postRepository.findPostWithHashtags(id).orElseThrow(PostNotFoundException::new);
        // 비밀글이면서 작성자와 방문자가 다를 경우
        if (post.isSecret() && !post.getUser().getId().equals(userId)) {
            throw new PostNotFoundException();
        }

        if (!postHitsService.isExistInSet(visitorId, post.getId())) {
            postHitsService.increaseData("viewCount_post_" + post.getId());
            postHitsService.addToSet(visitorId, post.getId());
        }
        return new PostResponse(post, userId);
    }

    /**
     * 최근 20개 게시글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getList() {
        return postRepository.findTop20ByOrderByCreatedAtDesc().stream()
                   .map(PostResponse::new)
                   .toList();
    }

    /**
     * 게시글 목록 조회
     */
    @Transactional(readOnly = true)
    public PagingResponse getPageList(PostPageSearchServiceRequest request) {
        Page<Post> postPage = postRepository.findByTitleContaining(request.keyword(),
            request.pageable());
        return new PagingResponse(postPage, request.userId());
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Long edit(Long id, PostEditServiceRequest request) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
        PostEditor postEditor = editorBuilder
                                    .title(request.title())
                                    .content(request.content())
                                    .isSecret(request.isSecret())
                                    .build();
        post.edit(postEditor);
        // 해시태그 수정
        updateHashtags(post, request.hashtags());
        linkFilesToPost(request.fileIds(), post);
        return post.getId();
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
    }

    /**
     * 해시태그를 이용하여 게시글 조회
     */
    @Transactional(readOnly = true)
    public PagingResponse getPostsByHashtag(HashtagSearchServiceRequest request) {
        Page<Post> postPage = postRepository.findByHashtagName(request.keyword(),
            request.pageable());
        return new PagingResponse(postPage, request.userId());
    }

    /**
     * 해시태그 수정
     */
    private void updateHashtags(Post post, List<String> newHashtags) {
        // 기존 해시태그 삭제
        post.getPostHashtags().clear();

        // 새로운 해시태그 저장 및 연결
        for (String tagName : newHashtags) {
            Hashtag hashtag = hashtagRepository.findByName(tagName)
                                  .orElseGet(() -> {
                                      Hashtag newHashtag = Hashtag.builder().name(tagName).build();
                                      return hashtagRepository.save(newHashtag);
                                  });
            PostHashtag postHashtag = PostHashtag.builder()
                                          .post(post)
                                          .hashtag(hashtag)
                                          .build();
            post.getPostHashtags().add(postHashtag);
        }
    }

    /**
     * 해시태그 저장
     */
    private void saveHashtag(List<String> hashtags, Post post) {
        for (String tagName : hashtags) {
            Hashtag hashtag = hashtagRepository.findByName(tagName)
                                  .orElseGet(() -> hashtagRepository.save(
                                      Hashtag.builder().name(tagName).build()));
            PostHashtag postHashtag = PostHashtag.builder().hashtag(hashtag).build();
            post.addPostHashtag(postHashtag);
        }
    }

    /**
     * 첨부파일 연결
     */
    private void linkFilesToPost(List<Long> fileIds, Post post) {
        for (Long id : fileIds) {
            UploadFile uploadFile = fileService.getFileEntityById(id);
            post.addFile(uploadFile);
        }
    }

}
