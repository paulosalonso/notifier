package com.github.paulosalonso.notifier.adapter.notifier.email;

import com.github.paulosalonso.notifier.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

import java.util.Optional;

import static com.github.paulosalonso.notifier.adapter.notifier.email.EmailNotificationProperty.IS_HTML_MESSAGE;

@RequiredArgsConstructor
public class SmtpEmailNotifier implements EmailNotifier {

    private final EmailProperties properties;
    private final JavaMailSender mailSender;

    @Override
    public void send(Notification message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            boolean isHtml = (boolean) message
                    .getAdditionalProperties()
                    .getOrDefault(IS_HTML_MESSAGE, false);

            helper.setFrom(Optional
                    .ofNullable(message.getSender())
                    .orElse(properties.sender));

            helper.setTo(message.getRecipients().toArray(new String[0]));
            helper.setSubject(message.getSubject());
            helper.setText(message.getMessage(), isHtml);

            mailSender.send(mimeMessage);
        } catch(Exception e) {
            throw new EmailException("Error sending mail.", e);
        }

    }
}
