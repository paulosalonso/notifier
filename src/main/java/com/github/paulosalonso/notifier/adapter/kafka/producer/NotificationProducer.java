package com.github.paulosalonso.notifier.adapter.kafka.producer;

import com.github.paulosalonso.notifier.kafka.avro.Notification;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationProducer {

    private final KafkaTemplate<String, Notification> kafkaTemplate;

    private static final String TOPIC = "com.github.paulosalonso.notifier.notifications";

    public void send(Notification notification) {
        ProducerRecord<String, Notification> record = new ProducerRecord<>(TOPIC, notification);
        kafkaTemplate.send(record);
    }
}
