package com.github.paulosalonso.notifier.usecase;

import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.usecase.port.NotifierPort;
import com.github.paulosalonso.notifier.usecase.port.SandboxPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.github.paulosalonso.notifier.usecase.port.NotifierPort.getNotifier;

@RequiredArgsConstructor
public class NotifyUseCase {

    private final List<NotifierPort> notifiers;
    private final SandboxPort sandbox;
    private final boolean sandboxEnabled;

    public void send(Notification notification) {
        if (sandboxEnabled) {
            sandbox.send(notification);
        } else {
            getNotifier(notifiers, notification.getType()).send(notification);
        }
    }

}
