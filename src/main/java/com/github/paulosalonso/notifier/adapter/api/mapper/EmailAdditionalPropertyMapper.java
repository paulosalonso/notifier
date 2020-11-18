package com.github.paulosalonso.notifier.adapter.api.mapper;

import com.github.paulosalonso.notifier.adapter.notifier.email.EmailNotificationProperty;
import com.github.paulosalonso.notifier.domain.NotificationAdditionalProperty;
import com.github.paulosalonso.notifier.domain.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class EmailAdditionalPropertyMapper implements NotificationAdditionalPropertyMapper {

    @Override
    public NotificationAdditionalProperty map(CharSequence propertyName) {
        try {
            return Enum.valueOf(EmailNotificationProperty.class, propertyName.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("'%s' is an invalid additional property name", propertyName));
        }
    }

    @Override
    public NotificationType attendedNotificationType() {
        return NotificationType.EMAIL;
    }
}
