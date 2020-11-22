package com.github.paulosalonso.notifier.usecase;

import com.github.paulosalonso.notifier.adapter.notifier.slack.SlackNotifier;
import com.github.paulosalonso.notifier.adapter.notifier.sms.SmsNotifier;
import com.github.paulosalonso.notifier.adapter.notifier.whatsapp.WhatsAppNotifier;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import com.github.paulosalonso.notifier.usecase.port.NotifierPort;
import com.github.paulosalonso.notifier.usecase.port.SandboxException;
import com.github.paulosalonso.notifier.usecase.port.SandboxPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SandboxPortTest {

    private SandboxPort sandbox;

    @Mock
    private NotifierPort emailNotifier;

    @Mock
    private SlackNotifier slackNotifier;

    @Mock
    private SmsNotifier smsNotifier;

    @Mock
    private WhatsAppNotifier whatsAppNotifier;

    List<NotifierPort> notifiers;

    private List<String> emailSandboxRecipients = List.of("email-sandbox-recipient");
    private List<String> slackSandboxRecipients = List.of("slack-sandbox-recipient");
    private List<String> smsSandboxRecipients = List.of("sms-sandbox-recipient");
    private List<String> whatsAppSandboxRecipients = List.of("whatsapp-sandbox-recipient");

    @BeforeEach
    void setup() {
        notifiers = List.of(emailNotifier, slackNotifier, smsNotifier, whatsAppNotifier);
        sandbox = spy(new SandboxPort(notifiers, emailSandboxRecipients,
                slackSandboxRecipients, smsSandboxRecipients, whatsAppSandboxRecipients));

        lenient().when(emailNotifier.attendedNotificationType()).thenReturn(NotificationType.EMAIL);
        lenient().when(slackNotifier.attendedNotificationType()).thenReturn(NotificationType.SLACK);
        lenient().when(smsNotifier.attendedNotificationType()).thenReturn(NotificationType.SMS);
        lenient().when(whatsAppNotifier.attendedNotificationType()).thenReturn(NotificationType.WHATSAPP);
    }

    @Test
    public void givenAEmailNotificationWhenSandboxNotifyCallEmailNotifierWithSandboxRecipient() {
        Notification notification = Notification.builder()
                .type(NotificationType.EMAIL)
                .recipient("original-recipient")
                .build();

        sandbox.send(notification);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(emailNotifier).send(notificationCaptor.capture());
        Notification notifierArgument = notificationCaptor.getValue();

        assertThat(notifierArgument.getRecipients()).isEqualTo(emailSandboxRecipients);

        verify(slackNotifier, never()).send(any(Notification.class));
        verify(smsNotifier, never()).send(any(Notification.class));
        verify(whatsAppNotifier, never()).send(any(Notification.class));
    }

    @Test
    public void givenASlackNotificationWhenSandboxNotifyCallSlackNotifierWithSandboxRecipient() {
        Notification notification = Notification.builder()
                .type(NotificationType.SLACK)
                .recipient("original-recipient")
                .build();

        sandbox.send(notification);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(slackNotifier).send(notificationCaptor.capture());
        Notification notifierArgument = notificationCaptor.getValue();

        assertThat(notifierArgument.getRecipients()).isEqualTo(slackSandboxRecipients);

        verify(emailNotifier, never()).send(any(Notification.class));
        verify(smsNotifier, never()).send(any(Notification.class));
        verify(whatsAppNotifier, never()).send(any(Notification.class));
    }

    @Test
    public void givenASmsNotificationWhenSandboxNotifyCallSmsNotifierWithSandboxRecipient() {
        Notification notification = Notification.builder()
                .type(NotificationType.SMS)
                .recipient("original-recipient")
                .build();

        sandbox.send(notification);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(smsNotifier).send(notificationCaptor.capture());
        Notification notifierArgument = notificationCaptor.getValue();

        assertThat(notifierArgument.getRecipients()).isEqualTo(smsSandboxRecipients);

        verify(emailNotifier, never()).send(any(Notification.class));
        verify(slackNotifier, never()).send(any(Notification.class));
        verify(whatsAppNotifier, never()).send(any(Notification.class));
    }

    @Test
    public void givenAWhatsAppNotificationWhenSandboxNotifyCallWhatsAppNotifierWithSandboxRecipient() {
        Notification notification = Notification.builder()
                .type(NotificationType.WHATSAPP)
                .recipient("original-recipient")
                .build();

        sandbox.send(notification);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(whatsAppNotifier).send(notificationCaptor.capture());
        Notification notifierArgument = notificationCaptor.getValue();

        assertThat(notifierArgument.getRecipients()).isEqualTo(whatsAppSandboxRecipients);

        verify(emailNotifier, never()).send(any(Notification.class));
        verify(slackNotifier, never()).send(any(Notification.class));
        verify(smsNotifier, never()).send(any(Notification.class));
    }

    @Test
    public void givenNullSandboxRecipientsWhenNotifiesThenThrowsSandboxException() {
        sandbox = new SandboxPort(notifiers, null, null, null, null);

        assertThatThrownBy(() -> sandbox.send(Notification.builder().type(NotificationType.EMAIL).build()))
                .isExactlyInstanceOf(SandboxException.class)
                .hasMessage("Sandbox recipients not configured to EMAIL notification type");
    }

    @Test
    public void givenEmptyListOfSandboxRecipientsWhenNotifiesThenThrowsSandboxException() {
        sandbox = new SandboxPort(notifiers, emptyList(), emptyList(), emptyList(), emptyList());

        assertThatThrownBy(() -> sandbox.send(Notification.builder().type(NotificationType.EMAIL).build()))
                .isExactlyInstanceOf(SandboxException.class)
                .hasMessage("Sandbox recipients not configured to EMAIL notification type");
    }
}
