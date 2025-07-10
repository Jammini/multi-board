package com.boardadminserver.user.application.response;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String email,
    String name,
    String nickname,
    LocalDateTime createdAt,
    boolean isActive
) {
    public UserResponse(com.boardadminserver.user.domain.User user) {
        this(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getNickname(),
            user.getCreatedAt(),
            user.isActive()
        );
    }
}
