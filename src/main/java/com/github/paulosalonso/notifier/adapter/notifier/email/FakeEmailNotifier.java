package com.github.paulosalonso.notifier.adapter.notifier.email;

import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailNotifier;
import com.github.paulosalonso.notifier.adapter.notifier.email.common.EmailProperties;
import com.github.paulosalonso.notifier.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class FakeEmailNotifier implements EmailNotifier {

	private final EmailProperties properties;

	@Override
	public void send(Notification message) {
		log.info("From: {}", Optional.ofNullable(message.getSender()).orElse(properties.sender));
		log.info("To: {}", String.join(", ", message.getRecipients()));
		log.info("Subject: {}", message.getSubject());
		log.info("Content: {}", message.getMessage());
	}

}
