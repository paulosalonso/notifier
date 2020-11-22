package com.github.paulosalonso.notifier.usecase;

import com.github.paulosalonso.notifier.adapter.notifier.slack.SlackNotifier;
import com.github.paulosalonso.notifier.adapter.notifier.whatsapp.WhatsAppNotifier;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import com.github.paulosalonso.notifier.usecase.port.NotifierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotifyUseCaseTest {

    private NotifyUseCase notifyUseCase;

    private List<NotifierPort> notifiers;

    @Mock
    private NotifierPort emailNotifier;

    @Mock
    private SlackNotifier slackNotifier;

    @Mock
    private WhatsAppNotifier whatsAppNotifier;

    @BeforeEach
    void setup() {
        notifiers = List.of(emailNotifier, slackNotifier, whatsAppNotifier);
        notifyUseCase = new NotifyUseCase(notifiers);

        lenient().when(emailNotifier.attendedNotificationType()).thenReturn(NotificationType.EMAIL);
        lenient().when(slackNotifier.attendedNotificationType()).thenReturn(NotificationType.SLACK);
        lenient().when(whatsAppNotifier.attendedNotificationType()).thenReturn(NotificationType.WHATSAPP);
    }

    @Test
    void givenANotificationWithEmailTypeWhenSendThenCallEmailNotifier() {
        Notification notification = Notification.builder()
                .type(NotificationType.EMAIL)
                .build();

        notifyUseCase.send(notification);

        verify(emailNotifier).attendedNotificationType();
        verify(emailNotifier).send(notification);
        verify(slackNotifier, never()).send(any(Notification.class));
        verify(whatsAppNotifier, never()).send(any(Notification.class));
    }

    @Test
    void givenANotificationWithSlackTypeWhenSendThenCallSlackNotifier() {
        Notification notification = Notification.builder()
                .type(NotificationType.SLACK)
                .build();

        notifyUseCase.send(notification);

        verify(slackNotifier).attendedNotificationType();
        verify(slackNotifier).send(notification);
        verify(emailNotifier, never()).send(any(Notification.class));
        verify(whatsAppNotifier, never()).send(any(Notification.class));
    }

    @Test
    void givenANotificationWithWhatsAppTypeWhenSendThenCallWhatsAppNotifier() {
        Notification notification = Notification.builder()
                .type(NotificationType.WHATSAPP)
                .build();

        notifyUseCase.send(notification);

        verify(whatsAppNotifier).attendedNotificationType();
        verify(whatsAppNotifier).send(notification);
        verify(emailNotifier, never()).send(any(Notification.class));
        verify(slackNotifier, never()).send(any(Notification.class));
    }

    @Test
    void givenANotificationWithoutTypeWhenSendThenThrowsIllegalArgumentException() {
        Notification notification = Notification.builder().build();

        assertThatThrownBy(() -> notifyUseCase.send(notification))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("The notification type is null or does not have a notifier implementation");

        verify(emailNotifier).attendedNotificationType();
    }
}
