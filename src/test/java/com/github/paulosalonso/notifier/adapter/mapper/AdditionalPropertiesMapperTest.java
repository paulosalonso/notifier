package com.github.paulosalonso.notifier.adapter.mapper;

import com.github.paulosalonso.notifier.adapter.api.mapper.EmailAdditionalPropertyMapper;
import com.github.paulosalonso.notifier.adapter.notifier.email.EmailNotificationProperty;
import com.github.paulosalonso.notifier.domain.NotificationAdditionalProperty;
import com.github.paulosalonso.notifier.domain.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AdditionalPropertiesMapperTest {

    private AdditionalPropertiesMapper additionalPropertiesMapper;

    @Spy
    private EmailAdditionalPropertyMapper emailAdditionalPropertyMapper;

    @BeforeEach
    void setup() {
        additionalPropertiesMapper = new AdditionalPropertiesMapper(List.of(emailAdditionalPropertyMapper));
    }

    @Test
    public void givenAEmailAdditionalPropertiesMapWithStringKeyAndValueWhenMapThenReturnMapWithTypedKeyAndValue() {
        Map<String, String> properties = Map.of("IS_HTML_MESSAGE", "true");

        Map<NotificationAdditionalProperty, Object> mappedProperties =
                additionalPropertiesMapper.map(NotificationType.EMAIL, properties);

        assertThat(mappedProperties)
                .hasSize(1)
                .containsOnlyKeys(EmailNotificationProperty.IS_HTML_MESSAGE)
                .containsValue(true);

        verify(emailAdditionalPropertyMapper).map("IS_HTML_MESSAGE");
    }

    @Test
    void givenAEmailAdditionalPropertiesMapWithInvalidValueWhenMapThenThrowsIllegalArgumentException() {
        Map<String, String> properties = Map.of("IS_HTML_MESSAGE", "invalid value");

        assertThatThrownBy(() -> additionalPropertiesMapper.map(NotificationType.EMAIL, properties))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("The value of 'IS_HTML_MESSAGE' property cannot be mapped to the expected value (Boolean)");

        verify(emailAdditionalPropertyMapper).map("IS_HTML_MESSAGE");
    }

    @Test
    void givenAEmailAdditionalPropertiesMapWithInvalidKeyWhenMapThenThrowsIllegalArgumentException() {
        Map<String, String> properties = Map.of("INVALID_KEY", "true");

        assertThatThrownBy(() -> additionalPropertiesMapper.map(NotificationType.EMAIL, properties))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("'INVALID_KEY' is an invalid additional property name");

        verify(emailAdditionalPropertyMapper).map("INVALID_KEY");
    }

    @Test
    void givenANullValueWhenMapThenReturnEmptyMap() {
        Map<NotificationAdditionalProperty, Object> mappedProperties =
                additionalPropertiesMapper.map(NotificationType.EMAIL, null);

        assertThat(mappedProperties).isEmpty();

        verifyNoInteractions(emailAdditionalPropertyMapper);
    }

    @Test
    void whenSendNullValueForTypeParameterThenThrowsIllegalArgumentException() {
        Map<String, String> properties = Map.of("INVALID_KEY", "true");

        assertThatThrownBy(() -> additionalPropertiesMapper.map(null, properties))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("The notification type is null or does not have a property mapper implementation");
    }
}
