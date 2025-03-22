package study.multiproject.api.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.multiproject.api.common.ApiResponse;
import study.multiproject.api.controller.user.converter.UserSignupRequestConverter;
import study.multiproject.api.controller.user.request.UserSignupRequest;
import study.multiproject.api.service.user.UserService;
import study.multiproject.api.service.user.response.UserResponse;
import study.multiproject.config.UserPrincipal;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserSignupRequestConverter userSignupRequestConverter;

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

}
