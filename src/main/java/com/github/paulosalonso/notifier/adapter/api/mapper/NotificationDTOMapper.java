package com.github.paulosalonso.notifier.adapter.api.mapper;

import com.github.paulosalonso.notifier.adapter.api.NotificationDTO;
import com.github.paulosalonso.notifier.adapter.mapper.AdditionalPropertiesMapper;
import com.github.paulosalonso.notifier.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationDTOMapper {

    private final AdditionalPropertiesMapper additionalPropertiesMapper;

    public Notification map(NotificationDTO dto) {
        return Notification.builder()
                .type(dto.getType())
                .sender(dto.getSender())
                .recipients(dto.getRecipients())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .additionalProperties(additionalPropertiesMapper.map(dto.getType(), dto.getAdditionalProperties()))
                .build();
    }

}
