package com.github.paulosalonso.notifier.domain;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class Notification {
    private NotificationType type;
    private String sender;
    @Singular
    private List<String> recipients;
    private String subject;
    private String message;
    @Singular(ignoreNullCollections = true)
    private Map<NotificationAdditionalProperty, Object> additionalProperties;

    public static NotificationBuilder builder() {
        return new NotificationBuilder() {
            @Override
            public Notification build() {
                Notification notification = super.build();

                notification.getAdditionalProperties()
                        .entrySet()
                        .forEach(this::validatePropertyValue);

                return notification;
            }

            private void validatePropertyValue(
                    Entry<? extends NotificationAdditionalProperty, Object> entry) {
                if (!entry.getValue().getClass().isAssignableFrom(entry.getKey().getPropertyValueType())) {
                    throw new IllegalArgumentException(String.format("The value of %s property must be of type %s",
                            ((Enum) entry.getKey()).name(), entry.getKey().getPropertyValueType().getSimpleName()));
                }
            }
        };
    }
}
