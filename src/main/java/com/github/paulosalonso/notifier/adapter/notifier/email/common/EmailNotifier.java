package com.github.paulosalonso.notifier.adapter.notifier.email.common;

import com.github.paulosalonso.notifier.domain.NotificationType;
import com.github.paulosalonso.notifier.usecase.port.NotifierPort;

public interface EmailNotifier extends NotifierPort {

    @Override
    default NotificationType attendedNotificationType() {
        return NotificationType.EMAIL;
    }
}
