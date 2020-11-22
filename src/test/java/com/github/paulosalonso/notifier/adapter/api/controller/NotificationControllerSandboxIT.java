package com.github.paulosalonso.notifier.adapter.api.controller;

import com.github.paulosalonso.notifier.adapter.api.NotificationDTO;
import com.github.paulosalonso.notifier.adapter.kafka.producer.NotificationProducer;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import com.github.paulosalonso.notifier.usecase.NotifyUseCase;
import com.github.paulosalonso.notifier.usecase.port.SandboxPort;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
        "notifier.email.service-type=smtp",
        "notifier.email.sendgrid.api-key=apiKey",
        "notifier.sandbox.enabled=true",
        "spring.kafka.bootstrap-servers=localhost:9092"
})
class NotificationControllerSandboxIT {

    @LocalServerPort
    private int port;

    @MockBean
    private SandboxPort sandbox;

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

        verify(sandbox).send(notificationCaptor.capture());

        Notification notification = notificationCaptor.getValue();
        assertThat(notification.getSender()).isEqualTo("sender");
        assertThat(notification.getRecipients())
                .hasSize(1)
                .first().isEqualTo("recipient");
        assertThat(notification.getSubject()).isEqualTo("Integrated test notification");
        assertThat(notification.getMessage()).isEqualTo("Message");
    }
}
