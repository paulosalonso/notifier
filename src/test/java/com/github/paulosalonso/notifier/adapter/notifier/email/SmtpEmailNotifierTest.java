package com.github.paulosalonso.notifier.adapter.notifier.email;

import com.github.paulosalonso.notifier.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmtpEmailNotifierTest {

    @InjectMocks
    private SmtpEmailNotifier smtpEmailNotifier;

    @Mock
    private EmailProperties emailProperties;

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @BeforeEach
    void setup() {
        emailProperties.sender = "default-sender";
    }

    @Test
    void givenAPlainTextEmailMessageWhenSendThenCallMailSender() throws MessagingException, IOException {
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        Notification notification = Notification.builder()
                .sender("sender")
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .build();

        smtpEmailNotifier.send(notification);

        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(mimeMessageCaptor.capture());

        MimeMessage mimeMessage = mimeMessageCaptor.getValue();
        assertThat(mimeMessage.getFrom())
                .hasSize(1)
                .anyMatch(address -> address.toString().equals("sender"));
        assertThat(mimeMessage.getAllRecipients())
                .hasSize(1)
                .anyMatch(address -> address.toString().equals("recipient"));
        assertThat(mimeMessage.getSubject()).isEqualTo("subject");
        assertThat(mimeMessage.getDataHandler().getContent()).isEqualTo("message");
        assertThat(mimeMessage.getDataHandler().getContentType()).startsWith("text/plain");

    }

    @Test
    void givenAHtmlEmailMessageWhenSendThenCallMailSender() throws MessagingException, IOException {
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        Notification notification = Notification.builder()
                .sender("sender")
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .additionalProperty(EmailNotificationProperty.IS_HTML_MESSAGE, true)
                .build();

        smtpEmailNotifier.send(notification);

        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(mimeMessageCaptor.capture());

        MimeMessage mimeMessage = mimeMessageCaptor.getValue();
        assertThat(mimeMessage.getFrom())
                .hasSize(1)
                .anyMatch(address -> address.toString().equals("sender"));
        assertThat(mimeMessage.getAllRecipients())
                .hasSize(1)
                .anyMatch(address -> address.toString().equals("recipient"));
        assertThat(mimeMessage.getSubject()).isEqualTo("subject");
        assertThat(mimeMessage.getDataHandler().getContent()).isEqualTo("message");
        assertThat(mimeMessage.getDataHandler().getContentType()).startsWith("text/html");

    }

    @Test
    void givenAnEmailMessageWithoutSenderWhenSendMessageThenUseDefaultSender() throws MessagingException {
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        Notification notification = Notification.builder()
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .additionalProperty(EmailNotificationProperty.IS_HTML_MESSAGE, true)
                .build();

        smtpEmailNotifier.send(notification);

        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(mimeMessageCaptor.capture());

        MimeMessage mimeMessage = mimeMessageCaptor.getValue();
        assertThat(mimeMessage.getFrom())
                .hasSize(1)
                .anyMatch(address -> address.toString().equals("default-sender"));
    }

    @Test
    void givenAnEmailMessageWhenJavaMailSenderThrowsExceptionThenThrowEmailException() {
        Notification notification = Notification.builder()
                .sender("sender")
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .additionalProperty(EmailNotificationProperty.IS_HTML_MESSAGE, true)
                .build();

        doThrow(new MailSendException("")).when(javaMailSender).send(any(MimeMessage.class));

        assertThatThrownBy(() -> smtpEmailNotifier.send(notification))
                .isExactlyInstanceOf(EmailException.class)
                .hasCauseExactlyInstanceOf(MailSendException.class);

        verify(javaMailSender).send(any(MimeMessage.class));
    }
}
