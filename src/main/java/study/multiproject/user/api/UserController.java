package study.multiproject.user.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.global.common.ApiResponse;
import study.multiproject.user.api.converter.ProfileUpdateRequestConverter;
import study.multiproject.user.api.converter.UserSignupRequestConverter;
import study.multiproject.user.api.request.ProfileUpdateRequest;
import study.multiproject.user.api.request.UserSignupRequest;
import study.multiproject.user.application.UserService;
import study.multiproject.user.application.response.UserResponse;
import study.multiproject.global.config.security.UserPrincipal;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserSignupRequestConverter userSignupRequestConverter;
    private final ProfileUpdateRequestConverter profileUpdateRequestConverter;

    /**
     * 회원가입
     */
    @PostMapping("/users/signup")
    public ApiResponse<Long> signup(@RequestBody @Valid UserSignupRequest request) {
        Long userId = userService.signup(userSignupRequestConverter.toServiceRequest(request));
        return ApiResponse.success(userId);
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/users/me")
    public ApiResponse<UserResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        UserResponse response = userService.getUser(principal.getUserId());
        return ApiResponse.success(response);
    }

    /**
     * 내 정보 수정
     */
    @PutMapping(value = "/users/me/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
        @ModelAttribute @Valid ProfileUpdateRequest request) {
        userService.updateProfile(principal.getUserId(), profileUpdateRequestConverter.toServiceRequest(request));
        return ApiResponse.success(null);
    }
}
