package com.github.paulosalonso.notifier.adapter.configuration;

import com.github.paulosalonso.notifier.usecase.NotifyUseCase;
import com.github.paulosalonso.notifier.usecase.port.NotifierPort;
import com.github.paulosalonso.notifier.usecase.port.SandboxPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class NotifyUseCaseConfiguration {

    private final SandboxProperties sandboxProperties;

    @Bean
    public NotifyUseCase notifyUseCase(List<NotifierPort> notifiers, SandboxPort sandbox) {
        return new NotifyUseCase(notifiers, sandbox, sandboxProperties.enabled);
    }
}
