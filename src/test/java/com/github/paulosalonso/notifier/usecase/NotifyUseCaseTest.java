package com.github.paulosalonso.notifier.usecase;

import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import com.github.paulosalonso.notifier.usecase.port.NotifierPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotifyUseCaseTest {

    private NotifyUseCase notifyUseCase;

    private List<NotifierPort> notifiers;

    @Mock
    private NotifierPort emailNotifier;

    @BeforeEach
    void setup() {
        notifiers = List.of(emailNotifier);
        notifyUseCase = new NotifyUseCase(notifiers);
        lenient().when(emailNotifier.attendedNotificationType()).thenReturn(NotificationType.EMAIL);
    }

    @Test
    void whenNotifyThenCallNotifierPort() {
        Notification notification = Notification.builder()
                .type(NotificationType.EMAIL)
                .build();

        notifyUseCase.send(notification);
        verify(emailNotifier).send(notification);
    }

    @Test
    void givenANotificationWithEmailTypeWhenSendThenCallEmailNotifier() {
        Notification notification = Notification.builder()
                .type(NotificationType.EMAIL)
                .build();

        notifyUseCase.send(notification);

        verify(emailNotifier).attendedNotificationType();
        verify(emailNotifier).send(notification);
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
