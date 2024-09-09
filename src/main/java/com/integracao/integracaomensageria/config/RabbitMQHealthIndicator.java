package com.integracao.integracaomensageria.config;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQHealthIndicator implements HealthIndicator {

	private final RabbitTemplate rabbitTemplate;

	public RabbitMQHealthIndicator(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public Health health() {
		try {
			rabbitTemplate.convertSendAndReceive("", "health", "ping");
			return Health.up().build();
		} catch (AmqpException e) {
			return Health.down().withException(e).build();
		}
	}

}
