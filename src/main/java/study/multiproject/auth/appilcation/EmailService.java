package study.multiproject.auth.appilcation;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String token, String resetUrl, Duration expiryMinutes) {
        String uri = UriComponentsBuilder
                         .fromHttpUrl(resetUrl)
                         .path(token)
                         .toUriString();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("비밀번호 재설정 안내");
        msg.setText(
            "안녕하세요,\n\n" +
            "아래 링크를 클릭하여 비밀번호를 재설정해 주세요:\n\n" +
            uri + "\n\n" +
            "이 링크는 " + expiryMinutes.toMinutes() + "분간 유효합니다."
        );
        mailSender.send(msg);
    }
}
