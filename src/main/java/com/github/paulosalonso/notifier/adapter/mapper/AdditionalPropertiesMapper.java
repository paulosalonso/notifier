package com.github.paulosalonso.notifier.adapter.mapper;

import com.github.paulosalonso.notifier.adapter.api.mapper.NotificationAdditionalPropertyMapper;
import com.github.paulosalonso.notifier.domain.NotificationAdditionalProperty;
import com.github.paulosalonso.notifier.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Component
public class AdditionalPropertiesMapper {

    private final List<NotificationAdditionalPropertyMapper> additionalPropertyMappers;
    private final ModelMapper mapper = new ModelMapper();

    public Map<NotificationAdditionalProperty, Object> map(NotificationType type, Map<? extends CharSequence, ? extends CharSequence> map) {
        if (map == null) {
            return new HashMap<>();
        }

        return map.keySet().stream()
                .map(property -> resolveProperty(type, property.toString()))
                .collect(toMap(
                        property -> property,
                        property -> resolveValue(property, map.get(((Enum) property).name()))));
    }

    private NotificationAdditionalProperty resolveProperty(NotificationType type, CharSequence propertyName) {
        return additionalPropertyMappers.stream()
                .filter(eachMapper -> eachMapper.attendedNotificationType().equals(type))
                .map(mapper -> mapper.map(propertyName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "The notification type is null or does not have a property mapper implementation"));
    }

    private <I, O> O resolveValue(NotificationAdditionalProperty property, I value) {
        try {
            return (O) mapper.map(value, property.getPropertyValueType());
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                    "The value of '%s' property cannot be mapped to the expected value (%s)",
                    ((Enum) property).name(),
                    property.getPropertyValueType().getSimpleName()));
        }
    }
}
