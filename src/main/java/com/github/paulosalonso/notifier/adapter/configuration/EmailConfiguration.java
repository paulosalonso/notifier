package com.github.paulosalonso.notifier.adapter.configuration;

import com.github.paulosalonso.notifier.adapter.notifier.email.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
@Configuration
public class EmailConfiguration {

    private final EmailProperties properties;

    @Bean
    public EmailNotifier emailNotifier(JavaMailSender javaMailSender) {
        switch (properties.serviceType) {
            case SMTP: return new SmtpEmailNotifier(properties, javaMailSender);
            case SANDBOX: return new SandboxEmailNotifier(properties, javaMailSender);
            default: return new FakeEmailNotifier(properties);
        }
    }
}
