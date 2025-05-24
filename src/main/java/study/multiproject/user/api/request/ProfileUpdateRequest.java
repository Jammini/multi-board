package study.multiproject.user.api.request;

import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record ProfileUpdateRequest(
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    String nickname,
    MultipartFile file
) {

}
