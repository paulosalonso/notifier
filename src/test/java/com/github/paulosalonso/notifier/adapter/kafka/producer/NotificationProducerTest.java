package com.github.paulosalonso.notifier.adapter.kafka.producer;

import com.github.paulosalonso.notifier.kafka.avro.Notification;
import com.github.paulosalonso.notifier.kafka.avro.NotificationType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationProducerTest {

    @InjectMocks
    private NotificationProducer producer;

    @Mock
    private KafkaTemplate<String, Notification> kafkaTemplate;

    @Test
    void givenANotificationWhenSendThenCallKafkaTemplate() {
        Notification avro = Notification.newBuilder()
                .setType(NotificationType.EMAIL)
                .setSender("sender")
                .setRecipients(List.of("recipient"))
                .setSubject("subject")
                .setMessage("message")
                .setAdditionalProperties(Map.of("key", "value"))
                .build();

        producer.send(avro);

        ArgumentCaptor<ProducerRecord<String, Notification>> recordCaptor = ArgumentCaptor.forClass(ProducerRecord.class);

        verify(kafkaTemplate).send(recordCaptor.capture());

        ProducerRecord<String, Notification> record = recordCaptor.getValue();

        assertThat(record.value()).isSameAs(avro);
    }
}
