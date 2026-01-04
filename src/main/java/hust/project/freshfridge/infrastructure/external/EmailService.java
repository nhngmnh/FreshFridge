package hust.project.freshfridge.infrastructure.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Async
    public void sendVerificationEmail(String to, String name, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Fresh Fridge - Xác thực email");

            String htmlContent = buildVerificationEmailTemplate(name, code);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Verification email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to: {}", to, e);
        }
    }

    private String buildVerificationEmailTemplate(String name, String code) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                        .content { padding: 20px; background-color: #f9f9f9; }
                        .code { font-size: 32px; font-weight: bold; color: #4CAF50; text-align: center; 
                                padding: 20px; background-color: #e8f5e9; border-radius: 8px; margin: 20px 0; }
                        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Fresh Fridge</h1>
                        </div>
                        <div class="content">
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Cảm ơn bạn đã đăng ký tài khoản Fresh Fridge. Vui lòng sử dụng mã sau để xác thực email:</p>
                            <div class="code">%s</div>
                            <p>Mã xác thực có hiệu lực trong 24 giờ.</p>
                            <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>
                        </div>
                        <div class="footer">
                            <p>© 2026 Fresh Fridge. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(name, code);
    }
}
