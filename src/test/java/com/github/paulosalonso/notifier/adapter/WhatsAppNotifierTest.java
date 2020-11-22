package com.github.paulosalonso.notifier.adapter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.paulosalonso.notifier.LoggerHelper;
import com.github.paulosalonso.notifier.adapter.notifier.whatsapp.WhatsAppNotifier;
import com.github.paulosalonso.notifier.domain.Notification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WhatsAppNotifierTest {

    private WhatsAppNotifier notifier = new WhatsAppNotifier();

    @Test
    public void whenSendMessageThenLog() {
        ListAppender<ILoggingEvent> appender = LoggerHelper.getListAppender(WhatsAppNotifier.class);

        notifier.send(Notification.builder()
                .recipient("recipient a")
                .recipient("recipient b")
                .message("test message")
                .build());

        assertThat(appender.list)
                .hasSize(1)
                .first().satisfies(event -> assertThat(event.getFormattedMessage())
                        .isEqualTo("Sending notification via WhatsApp to recipient a,recipient b. Message: test message"));
    }
}
