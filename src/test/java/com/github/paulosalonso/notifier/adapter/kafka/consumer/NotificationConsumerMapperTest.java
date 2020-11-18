package com.github.paulosalonso.notifier.adapter.kafka.consumer;

import com.github.paulosalonso.notifier.adapter.mapper.AdditionalPropertiesMapper;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerMapperTest {

    @InjectMocks
    private NotificationConsumerMapper notificationConsumerMapper;

    @Mock
    private AdditionalPropertiesMapper additionalPropertiesMapper;

    @Test
    void givenANotificationAvroWhenMapThenReturnDomainNotification() {
        com.github.paulosalonso.notifier.kafka.avro.Notification avro =
                com.github.paulosalonso.notifier.kafka.avro.Notification.newBuilder()
                        .setType(com.github.paulosalonso.notifier.kafka.avro.NotificationType.EMAIL)
                        .setSender("sender")
                        .setRecipients(List.of("recipient"))
                        .setSubject("subject")
                        .setMessage("message")
                        .setAdditionalProperties(Map.of("key", "value"))
                        .build();

        Notification domain = notificationConsumerMapper.map(avro);

        assertThat(domain.getType()).isEqualTo(NotificationType.EMAIL);
        assertThat(domain.getSender()).isEqualTo(avro.getSender());
        assertThat(domain.getRecipients())
                .hasSize(avro.getRecipients().size())
                .first()
                .isEqualTo(avro.getRecipients().get(0));
        assertThat(domain.getSubject()).isEqualTo(avro.getSubject());
        assertThat(domain.getMessage()).isEqualTo(avro.getMessage());

        verify(additionalPropertiesMapper).map(NotificationType.EMAIL, avro.getAdditionalProperties());
    }
}
