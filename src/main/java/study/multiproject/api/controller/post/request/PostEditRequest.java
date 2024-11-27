package study.multiproject.api.controller.post.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import study.multiproject.api.service.post.request.PostEditServiceRequest;

@Getter
public class PostEditRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String content;

    @Builder
    public PostEditRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditServiceRequest toServiceRequest() {
        return PostEditServiceRequest.builder()
                   .title(title)
                   .content(content)
                   .build();
    }
}
