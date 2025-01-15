package study.multiproject.api.service.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.api.service.post.exception.PostNotFoundException;
import study.multiproject.api.service.post.request.PostCreateServiceRequest;
import study.multiproject.api.service.post.request.PostEditServiceRequest;
import study.multiproject.api.service.post.request.PostPageSearchServiceRequest;
import study.multiproject.api.service.post.response.PagingResponse;
import study.multiproject.api.service.post.response.PostResponse;
import study.multiproject.domain.post.Post;
import study.multiproject.domain.post.PostRepository;

@Transactional
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("신규 게시글을 작성하면 아이디를 반환한다.")
    void createPost() {
        // given
        PostCreateServiceRequest request = new PostCreateServiceRequest("잼미니", "반포자이 살고싶다.", List.of("잼미니", "반포자이"), List.of());

        // when
        Long postId = postService.write(request);

        Post post = postRepository.findById(postId).get();

        // then
        assertThat(post.getId()).isEqualTo(postId);
    }


    @Test
    @DisplayName("게시글 단건을 조회한다.")
    void readPost() {
        // given
        Post post = Post.builder()
                        .title("잼미니")
                        .content("한남더힐 살고싶다.")
                        .build();
        postRepository.save(post);

        // when
        PostResponse result = postService.get(post.getId(), "visitorId");

        // then
        assertThat(post.getId()).isEqualTo(result.id());
        assertThat(post.getTitle()).isEqualTo(result.title());
        assertThat(post.getContent()).isEqualTo(result.content());
    }

    @Test
    @DisplayName("게시글 여러개를 조회한다.")
    void readAllPost() {
        // given
        Post post1 = Post.builder()
                         .title("잼미니1")
                         .content("내용입니다1")
                         .build();
        Post post2 = Post.builder()
                         .title("잼미니2")
                         .content("내용입니다2")
                         .build();
        postRepository.saveAll(List.of(post1, post2));

        // when
        List<PostResponse> result = postService.getList();

        // then
        assertThat(2L).isEqualTo(result.size());
    }

    @Test
    @DisplayName("게시글 여러개를 조회한다.")
    void readAllPagePost() {
        // given
        List<Post> posts = IntStream.range(1, 31)
                               .mapToObj(i -> Post.builder()
                                                  .title("잼미니 제목 " + i)
                                                  .content("내용입니다 " + i)
                                                  .build())
                               .toList();
        postRepository.saveAll(posts);

        PostPageSearchServiceRequest request = new PostPageSearchServiceRequest(
            PageRequest.of(0, 10, Sort.by("id").descending()), "");

        // when
        PagingResponse result = postService.getPageList(request);

        // then
        assertThat(10).isEqualTo(result.size());
        assertThat("잼미니 제목 30").isEqualTo(result.items().get(0).title());
        assertThat("잼미니 제목 21").isEqualTo(result.items().get(9).title());
    }

    @Test
    @DisplayName("게시글 제목을 수정한다.")
    void updatePost() {
        // given
        Post post = Post.builder()
                        .title("잼미니")
                        .content("판교 자이")
                        .build();
        postRepository.save(post);

        PostEditServiceRequest request = new PostEditServiceRequest("김정민", "판교 자이", null, null);

        // when
        Long postId = postService.edit(post.getId(), request);

        // then
        Post result = postRepository.findById(postId).get();

        assertThat(request.title()).isEqualTo(result.getTitle());
        assertThat(request.content()).isEqualTo(result.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
        // given
        Post post = Post.builder()
                        .title("잼미니")
                        .content("반포자이 살고싶다.")
                        .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertThat(0).isEqualTo(postRepository.count());
    }

    @Test
    @DisplayName("게시글을 조회하는데 존재하지 않는 게시글이라면 예외 처리한다.")
    void readWithoutPost() {
        // given
        Post post = Post.builder()
                        .title("잼미니")
                        .content("반포자이 살고싶다.")
                        .build();
        postRepository.save(post);

        // expected
        assertThatThrownBy(() -> postService.get(post.getId() + 1L, "visitorId"))
            .isInstanceOf(PostNotFoundException.class)
            .hasMessage("존재하지 않는 글입니다.");
    }

}
