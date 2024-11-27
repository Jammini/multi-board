package study.multiproject.api.controller.post.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import study.multiproject.api.service.post.request.PostCreateServiceRequest;

@Getter
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String content;

    @Builder
    public PostCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostCreateServiceRequest toServiceRequest() {
        return PostCreateServiceRequest.builder()
                   .title(title)
                   .content(content)
                   .build();
    }
}
