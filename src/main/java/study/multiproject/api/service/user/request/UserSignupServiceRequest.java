package study.multiproject.api.service.user.request;

import study.multiproject.domain.user.User;

public record UserSignupServiceRequest(
    String email,
    String password,
    String name
) {
    public User toEntity(String password) {
        return User.builder()
                   .email(email)
                   .password(password)
                   .name(name)
                   .build();
    }

}
