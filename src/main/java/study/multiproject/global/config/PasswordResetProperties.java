package study.multiproject.global.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.password-reset")
public class PasswordResetProperties {
    private String redisPrefix;
    private Duration expiryMinutes;
    private String resetUrl;
}
