package com.github.paulosalonso.notifier.adapter.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties("notifier.sandbox")
public class SandboxProperties {
    public boolean enabled;
    public Map<String, List<String>> recipients;
}
