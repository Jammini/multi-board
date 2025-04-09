package study.multiproject.user.application.request;

import study.multiproject.user.domain.User;

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
