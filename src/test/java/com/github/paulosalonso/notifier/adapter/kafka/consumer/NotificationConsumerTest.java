package com.github.paulosalonso.notifier.adapter.kafka.consumer;

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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Notification avro = Notification.newBuilder()
                .setType(NotificationType.EMAIL)
                .setSender("sender")
                .setRecipients(List.of("recipient"))
                .setSubject("subject")
                .setMessage("message")
                .setAdditionalProperties(Map.of("key", "value"))
                .build();

        com.github.paulosalonso.notifier.domain.Notification domain =
                com.github.paulosalonso.notifier.domain.Notification.builder().build();

        when(mapper.map(avro)).thenReturn(domain);

        consumer.consume(avro);

        verify(mapper).map(avro);
        verify(notifyUseCase).send(domain);
    }
}
