package com.github.paulosalonso.notifier.adapter.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties("notifier.sandbox")
public class SandboxProperties {
    public boolean enabled;
    public List<String> emailRecipients;
    public List<String> slackRecipients;
    public List<String> smsRecipients;
    public List<String> whatsAppRecipients;
}
