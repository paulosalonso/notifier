package com.github.paulosalonso.notifier.adapter.configuration;

import com.github.paulosalonso.notifier.usecase.port.NotifierPort;
import com.github.paulosalonso.notifier.usecase.port.SandboxPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SandboxConfiguration {

    private final SandboxProperties properties;

    @Bean
    public SandboxPort sandboxPort(List<NotifierPort> notifiers) {
        return new SandboxPort(notifiers, properties.recipients);
    }
}
