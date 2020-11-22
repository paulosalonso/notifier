package com.github.paulosalonso.notifier.adapter.notifier.email;

import com.github.paulosalonso.notifier.adapter.configuration.EmailConfiguration;
import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailNotifier;
import com.github.paulosalonso.notifier.adapter.configuration.EmailProperties;
import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailServiceType;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmailConfigurationTest {

    @InjectMocks
    private EmailConfiguration emailConfiguration;

    @Mock
    private EmailProperties emailProperties;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SendGrid sendGrid;

    @Test
    void givenSmtpEmailNotifierTypeConfigurationWhenRequestEmailNotifierBeanThenReturnSmtpImplementation() {
        emailProperties.serviceType = EmailServiceType.SMTP;
        EmailNotifier emailNotifier = emailConfiguration.emailNotifier(javaMailSender, sendGrid);
        assertThat(emailNotifier).isExactlyInstanceOf(SmtpEmailNotifier.class);

        Field mailSenderField = ReflectionUtils.findField(SmtpEmailNotifier.class, "mailSender");
        mailSenderField.setAccessible(true);
        assertThat(ReflectionUtils.getField(mailSenderField, emailNotifier)).isEqualTo(javaMailSender);

        Field propertiesField = ReflectionUtils.findField(SmtpEmailNotifier.class, "properties");
        propertiesField.setAccessible(true);
        assertThat(ReflectionUtils.getField(propertiesField, emailNotifier)).isEqualTo(emailProperties);
    }

    @Test
    void givenSendGridEmailNotifierTypeConfigurationWhenRequestEmailNotifierBeanThenReturnSendGridImplementation() {
        emailProperties.serviceType = EmailServiceType.SENDGRID;
        EmailNotifier emailNotifier = emailConfiguration.emailNotifier(javaMailSender, sendGrid);
        assertThat(emailNotifier).isExactlyInstanceOf(SendGridEmailNotifier.class);

        Field propertiesField = ReflectionUtils.findField(SendGridEmailNotifier.class, "properties");
        propertiesField.setAccessible(true);
        assertThat(ReflectionUtils.getField(propertiesField, emailNotifier)).isEqualTo(emailProperties);

        Field sendGridField = ReflectionUtils.findField(SendGridEmailNotifier.class, "sendGrid");
        sendGridField.setAccessible(true);
        assertThat(ReflectionUtils.getField(sendGridField, emailNotifier)).isEqualTo(sendGrid);
    }

    @Test
    public void givenASendGridApiKeyWhenGetBeanThenReturnSendGridInstance() {
        String sendGridApiKey = UUID.randomUUID().toString();

        SendGrid sendGrid = emailConfiguration.sendGrid(sendGridApiKey);

        Field apiKeyField = ReflectionUtils.findField(SendGrid.class, "apiKey");
        apiKeyField.setAccessible(true);
        String apiKey = (String) ReflectionUtils.getField(apiKeyField, sendGrid);

        assertThat(apiKey).isEqualTo(sendGridApiKey);
    }
}
