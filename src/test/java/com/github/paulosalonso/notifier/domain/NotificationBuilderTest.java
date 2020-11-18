package com.github.paulosalonso.notifier.domain;

import org.junit.jupiter.api.Test;

import static com.github.paulosalonso.notifier.adapter.notifier.email.EmailNotificationProperty.IS_HTML_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationBuilderTest {

    @Test
    void givenANotificationInvalidPropertyTypeThenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> Notification.builder()
                .additionalProperty(IS_HTML_MESSAGE, "String is invalid here")
                .build())
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenAEmailMessageBuilderWhenSetValidPropertyTypeThenBuildEmailMessage() {
        Notification emailMessage = Notification.builder()
                .additionalProperty(IS_HTML_MESSAGE, true)
                .build();

        assertThat(emailMessage.getAdditionalProperties())
                .hasSize(1)
                .containsKey(IS_HTML_MESSAGE)
                .containsValue(true);
    }
}
