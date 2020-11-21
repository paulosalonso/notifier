package com.github.paulosalonso.notifier.adapter.notifier.email.common;

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
    public EmailServiceType serviceType = EmailServiceType.FAKE;
    public Sandbox sandbox = new Sandbox();

    @Getter
    @Setter
    public class Sandbox {
        public List<String> recipients;
    }
}
