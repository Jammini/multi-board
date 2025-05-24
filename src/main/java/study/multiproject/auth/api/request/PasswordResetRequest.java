package study.multiproject.auth.api.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequest(
    @NotBlank(message = "이메일은 필수입니다.") String email
) {

}
