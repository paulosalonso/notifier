package com.github.paulosalonso.notifier.adapter.notifier.slack;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.paulosalonso.notifier.LoggerHelper;
import com.github.paulosalonso.notifier.adapter.notifier.whatsapp.WhatsAppNotifier;
import com.github.paulosalonso.notifier.domain.Notification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SlackNotifierTest {

    private SlackNotifier notifier = new SlackNotifier();

    @Test
    public void whenSendMessageThenLog() {
        ListAppender<ILoggingEvent> appender = LoggerHelper.getListAppender(SlackNotifier.class);

        notifier.send(Notification.builder()
                .recipient("recipient a")
                .recipient("recipient b")
                .message("test message")
                .build());

        assertThat(appender.list)
                .hasSize(1)
                .first().satisfies(event -> assertThat(event.getFormattedMessage())
                        .isEqualTo("Sending notification via Slack to recipient a,recipient b. Message: test message"));
    }
}
