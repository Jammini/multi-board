package study.multiproject.auth.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.auth.api.request.PasswordResetConfirmRequest;
import study.multiproject.auth.api.request.PasswordResetRequest;
import study.multiproject.auth.appilcation.AuthService;
import study.multiproject.auth.appilcation.response.PasswordResetResponse;
import study.multiproject.auth.exception.PasswordMismatchException;
import study.multiproject.global.common.ApiResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 비밀번호 재설정 요청
     */
    @PostMapping("/auth/password-reset/request")
    public ApiResponse<PasswordResetResponse> requestReset(@RequestBody @Valid PasswordResetRequest request) {
        PasswordResetResponse response = authService.requestPasswordReset(request.email());
        return ApiResponse.success(response);
    }

    /**
     * 비밀번호 재설정 확인
     */
    @PostMapping("/auth/password-reset/confirm/{token}")
    public ApiResponse<Void> confirmReset(@PathVariable String token, @RequestBody @Valid PasswordResetConfirmRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new PasswordMismatchException();
        }
        authService.confirmPasswordReset(token, request.newPassword());
        return ApiResponse.success(null);
    }
}
