package com.github.paulosalonso.notifier.adapter.notifier.whatsapp;

import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import com.github.paulosalonso.notifier.usecase.port.NotifierPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WhatsAppNotifier implements NotifierPort {

    @Override
    public void send(Notification notification) {
        log.info("Sending notification via WhatsApp to {}. Message: {}",
                String.join(",", notification.getRecipients()), notification.getMessage());
    }

    @Override
    public NotificationType attendedNotificationType() {
        return NotificationType.WHATSAPP;
    }
}
