package com.github.paulosalonso.notifier.adapter.kafka.consumer;

import com.github.paulosalonso.notifier.adapter.mapper.AdditionalPropertiesMapper;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class NotificationConsumerMapper {

    private final AdditionalPropertiesMapper additionalPropertiesMapper;

    public Notification map(com.github.paulosalonso.notifier.kafka.avro.Notification notification) {
        NotificationType type = NotificationType.valueOf(notification.getType().name());

        return Notification.builder()
                .type(type)
                .sender(notification.getSender().toString())
                .recipients(notification.getRecipients().stream()
                        .map(CharSequence::toString)
                        .collect(toList()))
                .subject(notification.getSubject().toString())
                .message(notification.getMessage().toString())
                .additionalProperties(additionalPropertiesMapper
                        .map(type, mapAdditionalProperties(notification.getAdditionalProperties())))
                .build();
    }

    private Map<String, CharSequence> mapAdditionalProperties(Map<CharSequence, CharSequence> properties) {
        Map<String, CharSequence> mappedProperties = new HashMap<>();
        properties.keySet().forEach(key -> mappedProperties.put(key.toString(), properties.get(key)));
        return mappedProperties;
    }
}
