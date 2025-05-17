package study.multiproject.auth.api.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetConfirmRequest(
    @NotBlank(message = "비밀번호는 필수입니다.")
    String newPassword,

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    String confirmPassword
) {

}
