package com.github.paulosalonso.notifier.adapter.api.mapper;

import com.github.paulosalonso.notifier.adapter.api.NotificationDTO;
import com.github.paulosalonso.notifier.adapter.mapper.AdditionalPropertiesMapper;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationDTOMapperTest {

    @InjectMocks
    private NotificationDTOMapper notificationDTOMapper;

    @Mock
    private AdditionalPropertiesMapper additionalPropertiesMapper;

    @Test
    void givenANotificationDTOWhenMapThenReturnNotification() {
        NotificationDTO dto = NotificationDTO.builder()
                .type(NotificationType.EMAIL)
                .sender("sender")
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .additionalProperty("IS_HTML_MESSAGE", "true")
                .build();

        Notification notification = notificationDTOMapper.map(dto);

        assertThat(notification.getType()).isEqualTo(NotificationType.EMAIL);
        assertThat(notification.getSender()).isEqualTo("sender");
        assertThat(notification.getRecipients())
                .hasSize(1)
                .first().isEqualTo("recipient");
        assertThat(notification.getSubject()).isEqualTo("subject");
        assertThat(notification.getMessage()).isEqualTo("message");

        verify(additionalPropertiesMapper).map(dto.getType(), dto.getAdditionalProperties());
    }
}
