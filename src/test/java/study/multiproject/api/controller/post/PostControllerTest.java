package study.multiproject.api.controller.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import study.multiproject.api.config.TestSecurityConfig;
import study.multiproject.post.api.PostController;
import study.multiproject.post.api.converter.PostCreateRequestConverter;
import study.multiproject.post.api.converter.PostEditRequestConverter;
import study.multiproject.post.api.converter.PostPageSearchRequestConverter;
import study.multiproject.post.api.request.PostCreateRequest;
import study.multiproject.post.api.request.PostEditRequest;
import study.multiproject.post.application.PostService;
import study.multiproject.post.application.request.PostCreateServiceRequest;
import study.multiproject.post.application.request.PostEditServiceRequest;
import study.multiproject.global.util.JwtTokenUtil;
import study.multiproject.global.config.security.filter.JwtAuthorizationFilter;
import study.multiproject.global.config.security.handler.CustomLogoutSuccessHandler;

@WithMockUser(username = "test@example.com", roles = "USER")
@Import(TestSecurityConfig.class)
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private PostCreateRequestConverter postCreateRequestConverter;

    @MockBean
    private PostEditRequestConverter postEditRequestConverter;

    @MockBean
    private PostPageSearchRequestConverter postPageSearchRequestConverter;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @MockBean
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Test
    @DisplayName("신규 게시글을 작성한다.")
    void createPost() throws Exception {
        PostCreateRequest request = new PostCreateRequest("잼미니", "잼미니는 잼잼이다.", false,null, null);
        PostCreateServiceRequest serviceRequest = new PostCreateServiceRequest("잼미니", "잼미니는 잼잼이다.", false,null, null, 1L);

        given(postService.write(any())).willReturn(1L);

        mockMvc.perform(post("/posts")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 한개를 조회한다.")
    void read() throws Exception {
        mockMvc.perform(get("/posts/{postId}", 1L)
                            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 여러개를 조회한다")
    void readAll() throws Exception {
        mockMvc.perform(get("/posts?page=1&size=3&sort=id,desc")
                            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 제목을 수정힌다.")
    void update() throws Exception {
        PostEditRequest request = new PostEditRequest("제목 수정합니다.", "내용입니다.", false, null, null);
        PostEditServiceRequest serviceRequest = new PostEditServiceRequest("제목 수정합니다.", "내용입니다.", false, null, null, 1L);

        given(postService.edit(any(), any())).willReturn(1L);

        mockMvc.perform(patch("/posts/{postId}", 1L)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() throws Exception {
        mockMvc.perform(delete("/posts/{postId}", 1L)
                            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
