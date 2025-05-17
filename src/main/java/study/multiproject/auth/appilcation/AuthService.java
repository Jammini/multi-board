package study.multiproject.auth.appilcation;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.multiproject.auth.appilcation.response.PasswordResetResponse;
import study.multiproject.auth.exception.InvalidTokenException;
import study.multiproject.global.config.MailProperties;
import study.multiproject.user.application.UserService;
import study.multiproject.user.domain.User;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final EmailService emailService;
    private final RedisTemplate<String, String> redisTemplate;
    private final MailProperties mailProperties;

    public PasswordResetResponse requestPasswordReset(String email) {
        User user = userService.getUserByEmail(email);

        // (1) 토큰 생성
        String token = UUID.randomUUID().toString();
        String redisKey = mailProperties.getRedisPrefix() + token;

        // (2) Redis에 저장
        redisTemplate.opsForValue().set(redisKey, user.getId().toString(), mailProperties.getExpiryMinutes(), TimeUnit.MINUTES);

        // (3) 이메일 전송
        emailService.sendEmail(user.getEmail(), token);
        return new PasswordResetResponse(user.getEmail(), mailProperties.getExpiryMinutes());
    }

    @Transactional
    public void confirmPasswordReset(String token, String newPassword) {
        String redisKey = mailProperties.getRedisPrefix() + token;

        // (1) 토큰 찾기
        String userIdStr = redisTemplate.opsForValue().get(redisKey);
        if (userIdStr == null) {
            throw new InvalidTokenException();
        }

        // (2) 비밀번호 변경
        Long userId = Long.valueOf(userIdStr);
        userService.changePassword(userId, newPassword);

        // (3) 일회용 토큰 삭제
        redisTemplate.delete(redisKey);
    }
}
