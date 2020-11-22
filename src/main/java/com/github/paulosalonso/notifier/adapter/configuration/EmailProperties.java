package com.github.paulosalonso.notifier.adapter.configuration;

import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailServiceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties("notifier.email")
public class EmailProperties {
    public String sender;
    public EmailServiceType serviceType = EmailServiceType.SMTP;
}
