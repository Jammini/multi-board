package study.multiproject.user.application.response;

import study.multiproject.user.domain.User;

public record UserResponse(
    Long id,
    String email,
    String name,
    String nickname,
    Long profileImageId
) {

    public UserResponse(User user) {
        this(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getNickname(),
            user.getProfileImageId()
        );
    }
}
