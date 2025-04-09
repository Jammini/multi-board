package study.multiproject.user.api.request;

import jakarta.validation.constraints.NotBlank;

public record UserSignupRequest(
    @NotBlank(message = "이메일은 필수입니다.") String email,
    @NotBlank(message = "비밀번호는 필수입니다.") String password,
    @NotBlank(message = "이름은 필수입니다.") String name
) {

}
