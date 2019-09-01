package io.vepo.access.email;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vepo.access.user.events.UserCreated;
import io.vepo.access.user.events.UserRemoved;

@ApplicationScoped
public class SendEmailHandler {

	private static final Logger logger = LoggerFactory.getLogger(SendEmailHandler.class);

	public void sendUserCreatedEmail(@Observes UserCreated userCreatedEvent) {
		logger.info("Sending User Created email! {}", userCreatedEvent);
	}

	public void sendUserRemovedEmail(@Observes UserRemoved userRemovedEvent) {
		logger.info("Sending User Removed email! {}", userRemovedEvent);
	}
}
