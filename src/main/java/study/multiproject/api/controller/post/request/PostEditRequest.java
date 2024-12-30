package study.multiproject.api.controller.post.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import study.multiproject.api.service.post.request.PostEditServiceRequest;

public record PostEditRequest(
    @NotBlank(message = "제목은 필수입니다.") String title,
    @NotBlank(message = "내용은 필수입니다.") String content,
    List<String> hashtags
) {
}
