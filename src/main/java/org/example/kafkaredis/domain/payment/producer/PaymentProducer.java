package org.example.kafkaredis.domain.payment.producer;

import static org.example.kafkaredis.common.model.kafka.topic.KafkaTopic.*;

import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * PaymentProducer 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@Service
@RequiredArgsConstructor
public class PaymentProducer {

	// Kafka에 메시지를 보내주는 역할을 수행할 것이다.
	// Spring Boot와 Kafka가 소통할 때 필요한 KafkaTemplate 을 선언
	private final KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedEventKafkaTemplate;

	public void send(PaymentCompletedEvent event) {
		// 토픽, 값을
		paymentCompletedEventKafkaTemplate.send(TOPIC_PAYMENT_COMPLETED, event);
	}
}