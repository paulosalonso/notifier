package com.github.paulosalonso.notifier.usecase;

import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import com.github.paulosalonso.notifier.usecase.port.NotifierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NotifyUseCase {

    private final List<NotifierPort> notifiers;

    public void send(Notification notification) {
        getNotifier(notification.getType()).send(notification);
    }

    private NotifierPort getNotifier(NotificationType type) {
        return notifiers.stream()
                .filter(notifier -> notifier.attendedNotificationType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "The notification type is null or does not have a notifier implementation"));
    }

}
