package com.github.paulosalonso.notifier.adapter.notifier.email;

import com.github.paulosalonso.notifier.domain.Notification;
import org.springframework.mail.javamail.JavaMailSender;

public class SandboxEmailNotifier extends SmtpEmailNotifier {

	private EmailProperties properties;

	public SandboxEmailNotifier(EmailProperties properties, JavaMailSender mailSender) {
		super(properties, mailSender);
		this.properties = properties;
	}

	@Override
	public void send(Notification message) {
		if (properties.sandbox.recipients == null) {
			throw new EmailException("Recipient (s) not configured for sandbox.");
		}

		message = message.toBuilder()
				.clearRecipients()
				.recipients(properties.sandbox.recipients)
				.build();

		super.send(message);
	}

}
