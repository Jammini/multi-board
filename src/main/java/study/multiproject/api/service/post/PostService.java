package study.multiproject.api.service.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.api.service.file.FileService;
import study.multiproject.api.service.file.request.FileData;
import study.multiproject.api.service.hashtag.request.HashtagSearchServiceRequest;
import study.multiproject.api.service.post.exception.PostNotFoundException;
import study.multiproject.api.service.post.request.PostCreateServiceRequest;
import study.multiproject.api.service.post.request.PostEditServiceRequest;
import study.multiproject.api.service.post.request.PostPageSearchServiceRequest;
import study.multiproject.api.service.post.response.PagingResponse;
import study.multiproject.api.service.post.response.PostResponse;
import study.multiproject.domain.file.UploadFile;
import study.multiproject.domain.hashtag.Hashtag;
import study.multiproject.domain.hashtag.HashtagRepository;
import study.multiproject.domain.post.Post;
import study.multiproject.domain.post.PostEditor;
import study.multiproject.domain.post.PostHashtag;
import study.multiproject.domain.post.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHitsService postHitsService;
    private final FileService fileService;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long write(PostCreateServiceRequest request) {
        Post post = postRepository.save(request.toEntity());
        linkFilesToPost(request.fileIds(), post);
        saveHashtag(request.hashtags(), post);
        return post.getId();
    }

    /**
     * 게시글 조회
     */
    @Transactional(readOnly = true)
    public PostResponse get(Long id, String visitorId) {
        Post post = postRepository.findPostWithHashtags(id).orElseThrow(PostNotFoundException::new);

        if (!postHitsService.isExistInSet(visitorId, post.getId())) {
            postHitsService.increaseData("viewCount_post_" + post.getId());
            postHitsService.addToSet(visitorId, post.getId());
        }
        return new PostResponse(post);
    }

    /**
     * 게시글 목록 전체 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
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
        return new PagingResponse(postPage);
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
        return new PagingResponse(postPage);
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

    /**
     * 첨부파일 수정
     */
    private void updateFiles(Post post, List<Long> filesToDelete, List<FileData> newFiles) {
        // 삭제할 파일 처리
        for (Long fileId : filesToDelete) {
            fileService.deleteFile(fileId);
            post.removeFileById(fileId);
        }
        // 새 파일 추가
//        saveFile(newFiles, post);
    }
}
