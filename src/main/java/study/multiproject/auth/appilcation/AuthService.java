package study.multiproject.auth.appilcation;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.auth.appilcation.response.PasswordResetResponse;
import study.multiproject.auth.exception.InvalidTokenException;
import study.multiproject.global.config.PasswordResetProperties;
import study.multiproject.user.application.UserService;
import study.multiproject.user.domain.User;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordResetProperties passwordResetProperties;

    public PasswordResetResponse requestPasswordReset(String email) {
        User user = userService.getUserByEmail(email);

        // 토큰 생성
        String token = UUID.randomUUID().toString();
        String redisKey = passwordResetProperties.getRedisPrefix() + token;

        // Redis에 저장
        redisTemplate.opsForValue().set(redisKey, user.getId().toString(), passwordResetProperties.getExpiryMinutes());

        // 이메일 전송
        emailService.sendEmail(user.getEmail(), token, passwordResetProperties.getResetUrl(), passwordResetProperties.getExpiryMinutes());
        return new PasswordResetResponse(user.getEmail(), passwordResetProperties.getExpiryMinutes());
    }

    @Transactional
    public void confirmPasswordReset(String token, String newPassword) {
        String redisKey = passwordResetProperties.getRedisPrefix() + token;

        // 토큰 찾기
        String userIdStr = redisTemplate.opsForValue().get(redisKey);
        if (userIdStr == null) {
            throw new InvalidTokenException();
        }

        // 비밀번호 변경
        Long userId = Long.valueOf(userIdStr);
        userService.changePassword(userId, newPassword);

        // 일회용 토큰 삭제
        redisTemplate.delete(redisKey);
    }
}
