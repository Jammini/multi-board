package study.multiproject.api.controller.post.request;

import jakarta.validation.constraints.NotBlank;
import study.multiproject.api.service.post.request.PostCreateServiceRequest;

public record PostCreateRequest(
    @NotBlank(message = "제목은 필수입니다.") String title,
    @NotBlank(message = "내용은 필수입니다.") String content
) {

    public PostCreateServiceRequest toServiceRequest() {
        return new PostCreateServiceRequest(title, content);
    }
}
