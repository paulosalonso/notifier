package com.github.paulosalonso.notifier.adapter.notifier.email;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.paulosalonso.notifier.LoggerHelper;
import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailProperties;
import com.github.paulosalonso.notifier.domain.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FakeEmailNotifierTest {

    @InjectMocks
    private FakeEmailNotifier fakeEmailNotifier;

    @Mock
    private EmailProperties emailProperties;

    @Test
    void givenAnEmailMessageWithSenderWhenSendThenLogMessageData() {
        ListAppender<ILoggingEvent> appender = LoggerHelper.getListAppender(FakeEmailNotifier.class);

        Notification notification = Notification.builder()
                .sender("sender")
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .build();

        fakeEmailNotifier.send(notification);

        List<ILoggingEvent> events = appender.list;

        assertThat(events).hasSize(4);
        assertThat(events.get(0).getFormattedMessage()).isEqualTo("From: sender");
        assertThat(events.get(1).getFormattedMessage()).isEqualTo("To: recipient");
        assertThat(events.get(2).getFormattedMessage()).isEqualTo("Subject: subject");
        assertThat(events.get(3).getFormattedMessage()).isEqualTo("Content: message");
    }

    @Test
    void givenAnEmailMessageWithoutSenderWhenSendThenLogMessageData() {
        emailProperties.sender = "default-sender";

        ListAppender<ILoggingEvent> appender = LoggerHelper.getListAppender(FakeEmailNotifier.class);

        Notification notification = Notification.builder()
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .build();

        fakeEmailNotifier.send(notification);

        List<ILoggingEvent> events = appender.list;

        assertThat(events).hasSize(4);
        assertThat(events.get(0).getFormattedMessage()).isEqualTo("From: default-sender");
        assertThat(events.get(1).getFormattedMessage()).isEqualTo("To: recipient");
        assertThat(events.get(2).getFormattedMessage()).isEqualTo("Subject: subject");
        assertThat(events.get(3).getFormattedMessage()).isEqualTo("Content: message");
    }
}
