package com.boardadminserver.user.application;

import com.boardadminserver.user.application.exception.UserNotFoundException;
import com.boardadminserver.user.application.request.UserActiveUpdateServiceRequest;
import com.boardadminserver.user.application.response.UserResponse;
import com.boardadminserver.user.dao.UserRepository;
import com.boardadminserver.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 목록 조회
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                   .map(UserResponse::new)
                   .toList();
    }

    /**
     * 회원 활성화 상태 변경
     */
    @Transactional
    public void updateUserActiveStatus(UserActiveUpdateServiceRequest request) {
        User user = userRepository.findById(request.id()).orElseThrow(
            () -> new UserNotFoundException("User not found with id: " + request.id()));
        if (request.isActive()) {
            user.activate();
        } else {
            user.deactivate();
        }
    }

    /**
     * 회원 검색
     */
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsersByKeyword(String keyword) {
        List<User> users = userRepository.searchByKeyword(keyword);
        return users.stream()
                   .map(UserResponse::new)
                   .toList();
    }
}
