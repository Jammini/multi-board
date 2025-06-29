package study.multiproject.post.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PostCreateRequest(
    @NotBlank(message = "제목은 필수입니다.") String title,
    @NotBlank(message = "내용은 필수입니다.") String content,
    @NotNull(message = "비밀글 여부는 필수입니다.") Boolean isSecret,
    List<String> hashtags,
    List<Long> fileIds,
    @NotNull(message = "카테고리는 필수입니다.") Long categoryId
) {

}
