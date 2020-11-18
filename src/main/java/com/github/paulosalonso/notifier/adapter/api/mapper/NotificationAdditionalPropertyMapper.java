package com.github.paulosalonso.notifier.adapter.api.mapper;

import com.github.paulosalonso.notifier.domain.NotificationAdditionalProperty;
import com.github.paulosalonso.notifier.domain.NotificationType;

public interface NotificationAdditionalPropertyMapper {
    NotificationAdditionalProperty map(CharSequence additionalPropertyName);
    NotificationType attendedNotificationType();
}
