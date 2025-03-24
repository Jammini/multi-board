package study.multiproject.api.service.user.response;

import study.multiproject.domain.user.User;

public record UserResponse(
    Long id,
    String email,
    String name
) {

    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getName());
    }
}
