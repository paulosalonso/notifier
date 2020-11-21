package com.github.paulosalonso.notifier.adapter.notifier.email.common;

import com.github.paulosalonso.notifier.domain.NotificationAdditionalProperty;

public enum EmailNotificationProperty implements NotificationAdditionalProperty {
    IS_HTML_MESSAGE(Boolean.class);

    Class<?> propertyValueType;

    EmailNotificationProperty(Class<?> propertyValueType) {
        this.propertyValueType = propertyValueType;
    }

    @Override
    public Class<?> getPropertyValueType() {
        return propertyValueType;
    }
}
