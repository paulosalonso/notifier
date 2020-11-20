package com.github.paulosalonso.notifier.adapter.kafka.consumer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.core.spi.FilterReply;
import com.github.paulosalonso.notifier.LoggerHelper;
import com.github.paulosalonso.notifier.kafka.avro.Notification;
import com.github.paulosalonso.notifier.kafka.avro.NotificationType;
import com.github.paulosalonso.notifier.usecase.NotifyUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationConsumerTest {

    @InjectMocks
    private NotificationConsumer consumer;

    @Mock
    private NotificationConsumerMapper mapper;

    @Mock
    private NotifyUseCase notifyUseCase;

    @Test
    void givenAMessageWhenConsumeThenMapToDomainAndCallUseCase() {
        Notification avro = buildNotificationAvro();

        com.github.paulosalonso.notifier.domain.Notification domain =
                com.github.paulosalonso.notifier.domain.Notification.builder().build();

        when(mapper.map(avro)).thenReturn(domain);

        consumer.consume(avro);

        verify(mapper).map(avro);
        verify(notifyUseCase).send(domain);
    }

    @Test
    public void givenAMessageWhenNotifyUseCaseThrowExceptionThenLogError() {
        Notification avro = buildNotificationAvro();

        ListAppender<ILoggingEvent> appender = LoggerHelper.getListAppender(NotificationConsumer.class);
        appender.addFilter(new Filter<ILoggingEvent>() {
            @Override
            public FilterReply decide(ILoggingEvent event) {
                if (Level.ERROR.equals(event.getLevel())) {
                    return FilterReply.ACCEPT;
                }

                return FilterReply.DENY;
            }
        });

        doThrow(RuntimeException.class).when(notifyUseCase).send(any());

        consumer.consume(avro);

        assertThat(appender.list).hasSize(2);
    }

    private Notification buildNotificationAvro() {
        return Notification.newBuilder()
                .setType(NotificationType.EMAIL)
                .setSender("sender")
                .setRecipients(List.of("recipient"))
                .setSubject("subject")
                .setMessage("message")
                .setAdditionalProperties(Map.of("key", "value"))
                .build();
    }
}
