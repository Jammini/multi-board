package study.multiproject.api.controller.post;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import study.multiproject.api.controller.post.request.PostCreateRequest;
import study.multiproject.api.controller.post.request.PostEditRequest;
import study.multiproject.api.service.post.PostService;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @DisplayName("신규 게시글을 작성한다.")
    void createPost() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                                        .title("잼미니")
                                        .content("잼미니는 잼잼이다.")
                                        .build();
        postService.write(request.toServiceRequest());

        // expected
        mockMvc.perform(post("/posts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 등록시 title 값은 필수이다.")
    void createPostCheckTitle() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                                        .content("잼미니는 잼잼이다.")
                                        .build();

        // expected
        mockMvc.perform(post("/posts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("제목은 필수입니다."));
    }

    @Test
    @DisplayName("게시글 등록시 Content 값은 필수이다.")
    void createPostCheckContent() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                                        .title("잼미니")
                                        .build();

        // expected
        mockMvc.perform(post("/posts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("내용은 필수입니다."));
    }

    @Test
    @DisplayName("게시글 한개를 조회한다.")
    void read() throws Exception {
        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 여러개를 조회한다.")
    void readAll() throws Exception {
        // expected
        mockMvc.perform(get("/posts")
                            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 제목을 수정힌다.")
    void update() throws Exception {
        // given
        PostEditRequest request = PostEditRequest.builder()
                                      .title("제목 수정합니다.")
                                      .content("내용입니다.")
                                      .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 등록시 Title 값은 필수이다.")
    void updatePostCheckTitle() throws Exception {
        // given
        PostEditRequest request = PostEditRequest.builder()
                                      .content("잼미니는 잼잼이다.")
                                      .build();

        // expected
        mockMvc.perform(post("/posts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("제목은 필수입니다."));
    }

    @Test
    @DisplayName("게시글 등록시 Content 값은 필수이다.")
    void updatePostCheckContent() throws Exception {
        // given
        PostEditRequest request = PostEditRequest.builder()
                                      .title("잼미니")
                                      .build();

        // expected
        mockMvc.perform(post("/posts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("내용은 필수입니다."));
    }


    @Test
    @DisplayName("게시글 삭제")
    void deletePost() throws Exception {
        // expected
        mockMvc.perform(delete("/posts/{postId}", 1L)
                            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }
}