package com.github.paulosalonso.notifier.usecase.port;

import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;

public interface NotifierPort {

    void send(Notification notification);
    NotificationType attendedNotificationType();
}
