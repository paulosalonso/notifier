package com.github.paulosalonso.notifier.usecase.port;

import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;

import java.util.List;
import java.util.Map;

import static com.github.paulosalonso.notifier.usecase.port.NotifierPort.getNotifier;

public class SandboxPort {

    private List<NotifierPort> notifiers;
    private Map<String, List<String>> sandboxRecipients;

    public SandboxPort(List<NotifierPort> notifiers, Map<String, List<String>> sandboxRecipients) {
        this.notifiers = notifiers;
        this.sandboxRecipients = sandboxRecipients;
    }

    public void send(Notification notification) {
        NotifierPort notifier = getNotifier(notifiers, notification.getType());

        Notification sandboxNotification = notification.toBuilder()
                .clearRecipients()
                .recipients(resolveRecipients(notification.getType()))
                .build();

        notifier.send(sandboxNotification);
    }

    private List<String> resolveRecipients(NotificationType type) {
        List<String> recipients = sandboxRecipients.get(type.name().toLowerCase());

        if (recipients == null || recipients.isEmpty()) {
            throw new SandboxException("Sandbox recipients not configured to " + type.toString() + " notification type");
        }

        return recipients;
    }
}
