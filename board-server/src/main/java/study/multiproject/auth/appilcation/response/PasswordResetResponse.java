package study.multiproject.auth.appilcation.response;

import java.time.Duration;

public record PasswordResetResponse(
    String email,
    Duration expiryTime
) {

}
