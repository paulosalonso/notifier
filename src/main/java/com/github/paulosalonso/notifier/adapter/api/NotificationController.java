package com.github.paulosalonso.notifier.adapter.api;

import com.github.paulosalonso.notifier.adapter.api.mapper.NotificationDTOMapper;
import com.github.paulosalonso.notifier.adapter.kafka.producer.NotificationProducer;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.kafka.avro.NotificationType;
import com.github.paulosalonso.notifier.usecase.NotifyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotifyUseCase notifyUseCase;
    private final NotificationDTOMapper mapper;
    private final NotificationProducer producer;
    
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void notify(@RequestBody @Valid NotificationDTO notificationDTO) {
        try {
            Notification notification = mapper.map(notificationDTO);
            notifyUseCase.send(notification);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/kafka")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void produce(@RequestBody @Valid NotificationDTO notificationDTO) {
        var notification = com.github.paulosalonso.notifier.kafka.avro.Notification.newBuilder()
                .setType(NotificationType.EMAIL)
                .setSender(notificationDTO.getSender())
                .setRecipients(notificationDTO.getRecipients().stream()
                        .map(recipient -> (CharSequence) recipient)
                        .collect(toList()))
                .setSubject(notificationDTO.getSubject())
                .setMessage(notificationDTO.getMessage())
                .setAdditionalProperties(mapProperties(notificationDTO.getAdditionalProperties()))
                .build();

        producer.send(notification);
    }

    private Map<CharSequence, CharSequence> mapProperties(Map<String, String> properties) {
        Map<CharSequence, CharSequence> mappedProperties = new HashMap<>();

        properties.keySet()
                .forEach(key -> mappedProperties.put(key, properties.get(key)));

        return mappedProperties;
    }

}
