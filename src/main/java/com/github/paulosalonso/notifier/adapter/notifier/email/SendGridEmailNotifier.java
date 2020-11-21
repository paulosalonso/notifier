package com.github.paulosalonso.notifier.adapter.notifier.email;

import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailException;
import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailNotifier;
import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailProperties;
import com.github.paulosalonso.notifier.domain.Notification;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

import static com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailNotificationProperty.IS_HTML_MESSAGE;

@Slf4j
public class SendGridEmailNotifier implements EmailNotifier {

    private final EmailProperties properties;
    private final SendGrid sendGrid;

    public SendGridEmailNotifier(EmailProperties properties, SendGrid sendGrid) {
        this.properties = properties;
        this.sendGrid = sendGrid;
    }

    @Override
    public void send(Notification notification) {
        try {
            log.info("Sending mail to {}", String.join(";", notification.getRecipients()));

            Request request = buildRequest(notification);
            Response response = sendGrid.api(request);

            log.info("Mail sending request result -> Status code: {} | Response body: {} | Headers: {}",
                    response.getStatusCode(), response.getBody(), response.getHeaders());
        } catch (IOException e) {
            log.error("Error sending mail.", e);
            throw new EmailException("Error sending mail.", e);
        }
    }

    private Request buildRequest(Notification notification) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(buildMail(notification).build());

        return request;
    }

    private Mail buildMail(Notification notification) {
        Mail mail = new Mail();

        String from = Optional.ofNullable(notification.getSender()).orElse(properties.sender);

        mail.setFrom(new Email(from));
        mail.setSubject(notification.getSubject());

        Personalization personalization = new Personalization();
        notification.getRecipients().stream()
                .map(Email::new)
                .forEach(personalization::addTo);
        mail.addPersonalization(personalization);

        mail.addContent(new Content(resolveType(notification), notification.getMessage()));

        return mail;
    }

    private String resolveType(Notification notification) {
        boolean isHtml = (boolean) notification
                .getAdditionalProperties()
                .getOrDefault(IS_HTML_MESSAGE, false);

        return isHtml ? "text/html" : "text/plain";
    }
}
