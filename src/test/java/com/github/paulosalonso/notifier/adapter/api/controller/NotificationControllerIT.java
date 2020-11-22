package com.github.paulosalonso.notifier.adapter.api.controller;

import com.github.paulosalonso.notifier.adapter.api.NotificationDTO;
import com.github.paulosalonso.notifier.adapter.kafka.producer.NotificationProducer;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import com.github.paulosalonso.notifier.usecase.NotifyUseCase;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
        "notifier.email.service-type=smtp",
        "notifier.email.sendgrid.api-key=apiKey",
        "spring.kafka.bootstrap-servers=localhost:9092"
})
class NotificationControllerIT {

    @LocalServerPort
    private int port;

    @MockBean
    private NotifyUseCase notifyUseCase;

    @MockBean
    private NotificationProducer producer;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void whenPostAEmailNotificationThenReturnAcceptedAndCallEmailNotifier() {
        given()
                .contentType("application/json")
                .body(NotificationDTO.builder()
                        .type(NotificationType.EMAIL)
                        .sender("sender")
                        .recipient("recipient")
                        .subject("Integrated test notification")
                        .message("Message")
                        .build())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notifyUseCase).send(notificationCaptor.capture());

        Notification notification = notificationCaptor.getValue();
        assertThat(notification.getSender()).isEqualTo("sender");
        assertThat(notification.getRecipients())
                .hasSize(1)
                .first().isEqualTo("recipient");
        assertThat(notification.getSubject()).isEqualTo("Integrated test notification");
        assertThat(notification.getMessage()).isEqualTo("Message");
    }

    @Test
    void whenPostANotificationWithInvalidAdditionalPropertyKeyThenReturnBadRequest() {
        given()
                .contentType("application/json")
                .body(NotificationDTO.builder()
                        .type(NotificationType.EMAIL)
                        .sender("mail@mail.com")
                        .recipient("recipient@mail.com")
                        .subject("Integrated test notification")
                        .message("Message")
                        .additionalProperty("NONEXISTENT_PROPERTY", "false")
                        .build())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("type", equalTo("https://github.com/paulosalonso/errors/invalid-data"))
                .body("title", equalTo("Invalid data"))
                .body("detail", equalTo("'NONEXISTENT_PROPERTY' is an invalid additional property name"))
                .body("timestamp", notNullValue());
    }

    @Test
    void whenPostANotificationWithInvalidAdditionalPropertyValueThenReturnBadRequest() {
        given()
                .contentType("application/json")
                .body(NotificationDTO.builder()
                        .type(NotificationType.EMAIL)
                        .sender("sender")
                        .recipient("recipient")
                        .subject("Integrated test notification")
                        .message("Message")
                        .additionalProperty("IS_HTML_MESSAGE", "String is an invalid value")
                        .build())
                .when()
                .post("/notifications")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("type", equalTo("https://github.com/paulosalonso/errors/invalid-data"))
                .body("title", equalTo("Invalid data"))
                .body("detail", equalTo("The value of 'IS_HTML_MESSAGE' property cannot be mapped to the expected value (Boolean)"))
                .body("timestamp", notNullValue());
    }

    @Test
    void whenPostANotificationForKafkaThenReturnAcceptedAndCallProducer() {
        given()
                .contentType("application/json")
                .body(NotificationDTO.builder()
                        .type(NotificationType.EMAIL)
                        .sender("sender")
                        .recipient("recipient")
                        .subject("Integrated test notification")
                        .message("Message")
                        .additionalProperty("IS_HTML_MESSAGE", "false")
                        .build())
                .when()
                .post("/notifications/kafka")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        ArgumentCaptor<com.github.paulosalonso.notifier.kafka.avro.Notification> notificationCaptor =
                ArgumentCaptor.forClass(com.github.paulosalonso.notifier.kafka.avro.Notification.class);
        verify(producer).send(notificationCaptor.capture());

        com.github.paulosalonso.notifier.kafka.avro.Notification notification = notificationCaptor.getValue();
        assertThat(notification.getSender()).isEqualTo("sender");
        assertThat(notification.getRecipients())
                .hasSize(1)
                .first().isEqualTo("recipient");
        assertThat(notification.getSubject()).isEqualTo("Integrated test notification");
        assertThat(notification.getMessage()).isEqualTo("Message");
    }
}
