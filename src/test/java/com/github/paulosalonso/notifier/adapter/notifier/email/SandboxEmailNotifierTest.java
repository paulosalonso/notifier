package com.github.paulosalonso.notifier.adapter.notifier.email;

import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailException;
import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailProperties;
import com.github.paulosalonso.notifier.domain.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SandboxEmailNotifierTest {

    @InjectMocks
    private SandboxEmailNotifier sandboxEmailNotifier;

    @Spy
    private EmailProperties emailProperties;

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @Test
    void givenAEmailMessageWithRecipientsWhenSendThenReplaceWithSandboxConfiguredRecipient() throws MessagingException {
        emailProperties.sandbox.recipients = List.of("sandbox-recipient");
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        Notification notification = Notification.builder()
                .sender("sender")
                .recipient("recipient-a")
                .recipient("recipient-b")
                .subject("subject")
                .message("message")
                .build();

        sandboxEmailNotifier.send(notification);

        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(mimeMessageCaptor.capture());

        MimeMessage mimeMessage = mimeMessageCaptor.getValue();
        assertThat(mimeMessage.getAllRecipients())
                .hasSize(1)
                .anyMatch(address -> address.toString().equals("sandbox-recipient"));

    }

    @Test
    void givenNonConfiguredSandboxRecipientsWhenSendEmailMessageThenThrowEmailException() {
        emailProperties.sandbox.recipients = null;

        Notification notification = Notification.builder()
                .sender("sender")
                .recipient("recipient-a")
                .recipient("recipient-b")
                .subject("subject")
                .message("message")
                .build();

        assertThatThrownBy(() -> sandboxEmailNotifier.send(notification))
                .isExactlyInstanceOf(EmailException.class)
                .hasMessage("Recipient (s) not configured for sandbox.");

        verifyNoInteractions(javaMailSender);
    }
}
