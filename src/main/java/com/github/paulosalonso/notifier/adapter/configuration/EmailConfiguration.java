package com.github.paulosalonso.notifier.adapter.configuration;

import com.github.paulosalonso.notifier.adapter.notifier.email.*;
import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailNotifier;
import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailProperties;
import com.sendgrid.SendGrid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
@Configuration
public class EmailConfiguration {

    private final EmailProperties properties;

    @Bean
    public EmailNotifier emailNotifier(JavaMailSender javaMailSender, SendGrid sendGrid) {
        switch (properties.serviceType) {
            case SMTP: return new SmtpEmailNotifier(properties, javaMailSender);
            case SENDGRID: return new SendGridEmailNotifier(properties, sendGrid);
            case SANDBOX: return new SandboxEmailNotifier(properties, javaMailSender);
            default: return new FakeEmailNotifier(properties);
        }
    }

    @Bean
    public SendGrid sendGrid(@Value("${notifier.email.sendgrid.api-key}") String apiKey) {
        return new SendGrid(apiKey);
    }
}
