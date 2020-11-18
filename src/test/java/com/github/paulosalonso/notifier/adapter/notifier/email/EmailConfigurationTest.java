package com.github.paulosalonso.notifier.adapter.notifier.email;

import com.github.paulosalonso.notifier.adapter.configuration.EmailConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmailConfigurationTest {

    @InjectMocks
    private EmailConfiguration emailConfiguration;

    @Mock
    private EmailProperties emailProperties;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void givenFakeEmailNotifierTypeConfigurationWhenRequestEmailNotifierBeanThenReturnFakeImplementation() {
        emailProperties.serviceType = EmailServiceType.FAKE;
        EmailNotifier emailNotifier = emailConfiguration.emailNotifier(javaMailSender);
        assertThat(emailNotifier).isExactlyInstanceOf(FakeEmailNotifier.class);
    }

    @Test
    void givenSandboxEmailNotifierTypeConfigurationWhenRequestEmailNotifierBeanThenReturnSandboxImplementation() {
        emailProperties.serviceType = EmailServiceType.SANDBOX;
        EmailNotifier emailNotifier = emailConfiguration.emailNotifier(javaMailSender);
        assertThat(emailNotifier).isExactlyInstanceOf(SandboxEmailNotifier.class);
    }

    @Test
    void givenSmtpEmailNotifierTypeConfigurationWhenRequestEmailNotifierBeanThenReturnSmtpImplementation() {
        emailProperties.serviceType = EmailServiceType.SMTP;
        EmailNotifier emailNotifier = emailConfiguration.emailNotifier(javaMailSender);
        assertThat(emailNotifier).isExactlyInstanceOf(SmtpEmailNotifier.class);
    }
}
