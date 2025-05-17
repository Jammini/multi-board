package study.multiproject.auth.appilcation.response;

public record PasswordResetResponse(
    String email,
    long expiryMinute
) {

}
