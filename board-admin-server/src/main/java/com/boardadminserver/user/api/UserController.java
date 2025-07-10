package com.boardadminserver.user.api;

import com.boardadminserver.user.api.converter.UserActiveUpdateRequestConverter;
import com.boardadminserver.user.api.request.UserActiveUpdateRequest;
import com.boardadminserver.user.application.UserService;
import com.boardadminserver.user.application.response.UserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserActiveUpdateRequestConverter userActiveUpdateRequestConverter;

    /**
     * 회원 목록 조회
     */
    @GetMapping("/users")
    public List<UserResponse> getUsers(@RequestParam(required = false) String keyword) {
        return (keyword == null || keyword.isBlank())
                   ? userService.getUsers()
                   : userService.searchUsersByKeyword(keyword);
    }

    /**
     * 회원 활성화 상태 변경
     */
    @PutMapping("/users/{userId}")
    public void updateUserActiveStatus(@PathVariable Long userId, @RequestBody UserActiveUpdateRequest request) {
        userService.updateUserActiveStatus(userActiveUpdateRequestConverter.toServiceRequest(userId, request));
    }
}
