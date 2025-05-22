package study.multiproject.auth.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.AssertTrue;

public record PasswordResetConfirmRequest(
    @NotBlank(message = "비밀번호는 필수입니다.")
    String newPassword,

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    String confirmPassword
) {
    @AssertTrue(message = "비밀번호와 확인용 비밀번호가 일치하지 않습니다.")
    public boolean isPasswordMatching() {
        return newPassword.equals(confirmPassword);
    }
}
