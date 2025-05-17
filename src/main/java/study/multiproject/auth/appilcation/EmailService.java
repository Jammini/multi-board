package study.multiproject.auth.appilcation;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import study.multiproject.global.config.MailProperties;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public void sendEmail(String to, String token) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("비밀번호 재설정 안내");
        msg.setText(
            "안녕하세요,\n\n" +
            "아래 링크를 클릭하여 비밀번호를 재설정해 주세요:\n\n" +
            mailProperties.getResetUrl() + token + "\n\n" +
            "이 링크는 " + mailProperties.getExpiryMinutes() + "분간 유효합니다."
        );
        mailSender.send(msg);
    }
}
