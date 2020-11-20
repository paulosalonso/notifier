package com.github.paulosalonso.notifier.adapter.notifier.email;

import com.github.paulosalonso.notifier.adapter.api.mapper.EmailAdditionalPropertyMapper;
import com.github.paulosalonso.notifier.adapter.configuration.EmailConfiguration;
import com.github.paulosalonso.notifier.domain.Notification;
import com.github.paulosalonso.notifier.domain.NotificationType;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.github.paulosalonso.notifier.adapter.notifier.email.EmailNotificationProperty.IS_HTML_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendGridEmailNotifierTest {

    @InjectMocks
    private SendGridEmailNotifier notifier;

    @Mock
    private SendGrid sendGrid;

    @Mock
    private EmailProperties properties;

    private static final String EXPECTED_PLAIN_BODY = "{\"from\":{\"email\":\"sender\"},\"subject\":\"subject\",\"personalizations\":[{\"to\":[{\"email\":\"recipient\"}]}],\"content\":[{\"type\":\"text/plain\",\"value\":\"message\"}]}";
    private static final String EXPECTED_HTML_BODY = "{\"from\":{\"email\":\"sender\"},\"subject\":\"subject\",\"personalizations\":[{\"to\":[{\"email\":\"recipient\"}]}],\"content\":[{\"type\":\"text/html\",\"value\":\"message\"}]}";

    @BeforeEach
    public void setup() throws IOException {
        lenient().when(sendGrid.api(any(Request.class))).thenReturn(mock(Response.class));
    }

    @Test
    public void givenAPlainTextNotificationWhenSendThenCallSendGridApiMethod() throws IOException {
        Notification notification = Notification.builder()
                .type(NotificationType.EMAIL)
                .sender("sender")
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .build();

        notifier.send(notification);

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        verify(sendGrid).api(requestCaptor.capture());

        Request request = requestCaptor.getValue();

        assertThat(request.getMethod()).isEqualTo(Method.POST);
        assertThat(request.getEndpoint()).isEqualTo("mail/send");
        assertThat(request.getBody()).isEqualTo(EXPECTED_PLAIN_BODY);
    }

    @Test
    public void givenAHtmlNotificationWhenSendThenCallSendGridApiMethod() throws IOException {
        Notification notification = Notification.builder()
                .type(NotificationType.EMAIL)
                .sender("sender")
                .recipient("recipient")
                .subject("subject")
                .message("message")
                .additionalProperty(IS_HTML_MESSAGE, true)
                .build();

        notifier.send(notification);

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        verify(sendGrid).api(requestCaptor.capture());

        Request request = requestCaptor.getValue();

        assertThat(request.getMethod()).isEqualTo(Method.POST);
        assertThat(request.getEndpoint()).isEqualTo("mail/send");
        assertThat(request.getBody()).isEqualTo(EXPECTED_HTML_BODY);
    }

    @Test
    public void givenANotificationWhenSendGridThrowAnExceptionThenThrowsEmailException() throws IOException {
        when(sendGrid.api(any(Request.class))).thenThrow(IOException.class);

        assertThatThrownBy(() -> notifier.send(Notification.builder().build()))
                .isExactlyInstanceOf(EmailException.class)
                .hasMessage("Error sending mail.")
                .hasCauseExactlyInstanceOf(IOException.class);
    }
}
