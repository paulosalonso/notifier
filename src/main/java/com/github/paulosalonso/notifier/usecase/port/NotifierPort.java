package com.github.paulosalonso.notifier.usecase.port;

import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;

import java.util.List;

public interface NotifierPort {

    void send(Notification notification);
    NotificationType attendedNotificationType();

    static NotifierPort getNotifier(List<NotifierPort> notifiers, NotificationType type) {
        return notifiers.stream()
                .filter(notifier -> notifier.attendedNotificationType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "The notification type is null or does not have a notifier implementation"));
    }
}
