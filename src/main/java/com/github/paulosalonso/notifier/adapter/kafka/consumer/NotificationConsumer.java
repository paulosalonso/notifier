package com.github.paulosalonso.notifier.adapter.kafka.consumer;

import com.github.paulosalonso.notifier.kafka.avro.Notification;
import com.github.paulosalonso.notifier.usecase.NotifyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationConsumer {

    private final NotifyUseCase notifyUseCase;
    private final NotificationConsumerMapper mapper;

    @KafkaListener(topics = "com.github.paulosalonso.notifier.notifications")
    public void consume(Notification notification) {
        log.debug("Sending notification received from Kafka: {}", notification.toString());

        try {
            notifyUseCase.send(mapper.map(notification));
        } catch (Exception e) {
            log.error("NOTIFY_USECASE_ERROR", e);
            log.error("NOTIFY_USECASE_ERROR_MESSAGE_PAYLOAD: {}", notification.toString());
        }
    }

}
