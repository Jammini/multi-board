package study.multiproject.user.application.response;

import study.multiproject.user.domain.User;

public record UserResponse(
    Long id,
    String email,
    String name,
    String nickname,
    String profileFilePath
) {

    public UserResponse(User user) {
        this(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getNickname(),
            user.getProfileImage() != null ? user.getProfileImage().getFilePath(): null
        );
    }
}
