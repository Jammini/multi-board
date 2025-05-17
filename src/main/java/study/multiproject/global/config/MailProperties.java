package study.multiproject.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mail.reset-token")
public class MailProperties {
    private String redisPrefix;
    private long expiryMinutes;
    private String resetUrl;
}
