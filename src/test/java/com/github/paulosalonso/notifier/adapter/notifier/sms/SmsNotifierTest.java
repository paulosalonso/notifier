package com.github.paulosalonso.notifier.adapter.notifier.sms;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.paulosalonso.notifier.LoggerHelper;
import com.github.paulosalonso.notifier.adapter.notifier.slack.SlackNotifier;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SmsNotifierTest {

    private SmsNotifier notifier = new SmsNotifier();

    @Test
    public void whenSendMessageThenLog() {
        ListAppender<ILoggingEvent> appender = LoggerHelper.getListAppender(SmsNotifier.class);

        notifier.send(Notification.builder()
                .recipient("+5500000000000")
                .recipient("+5511111111111")
                .message("test message")
                .build());

        assertThat(appender.list)
                .hasSize(1)
                .first().satisfies(event -> assertThat(event.getFormattedMessage())
                        .isEqualTo("Sending notification via SMS to +5500000000000,+5511111111111. Message: test message"));
    }

    @Test
    public void whenGetAttendedNotificationTypeThenReturnSlack() {
        assertThat(notifier.attendedNotificationType()).isEqualTo(NotificationType.SMS);
    }
}
