package org.example.kafkaredis.common.config.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

/**
 * KafkaConsumerConfig 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@Configuration
public class KafkaConsumerConfig {

	// Kafka 주소
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootStrapServers;

	// Consume한 정보를 기준으로 오늘 가장 많이 판매된 상품의 랭킹을 구하는 작업을 수행할 예정
	// ConsumerFactory 만들어 줄 예정이다! <String, PaymentCompletedEvent>
	@Bean
	public ConsumerFactory<String, PaymentCompletedEvent> productRankingConsumerFactory() {

		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "product-ranking-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		// 지금부터 들어오는 값부터 받을 것이다.
		// 처음부터 메시지를 읽지 않는다.
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

		JsonDeserializer<PaymentCompletedEvent> deserializer = new JsonDeserializer<>(PaymentCompletedEvent.class);

		return new DefaultKafkaConsumerFactory<>(
			props,
			new StringDeserializer(),
			deserializer
		);
	}

	// ListenerContainer -> Kafka Topic에 값이 들어왓는지 안들어왔는지 감지
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> productRankingKafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(productRankingConsumerFactory());

		return factory;
	}

	// 결제 기록 전용 컨슈머 그룹을 만들어줘야 합니다.
	@Bean
	public ConsumerFactory<String, PaymentCompletedEvent> paymentHistoryConsumerFactory() {

		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-history-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		// 지금부터 들어오는 값부터 받을 것이다.
		// 처음부터 메시지를 읽지 않는다.
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

		JsonDeserializer<PaymentCompletedEvent> deserializer = new JsonDeserializer<>(PaymentCompletedEvent.class);

		return new DefaultKafkaConsumerFactory<>(
			props,
			new StringDeserializer(),
			deserializer
		);
	}

	// Consumer Group 및 Factory가 동작할 수 있게 하는 listener container factory도 만들어줘야 함
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> paymentHistoryKafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(paymentHistoryConsumerFactory());

		return factory;
	}

	// 배송 컨슈머 팩토리
	@Bean
	public ConsumerFactory<String, PaymentCompletedEvent> deliveryConsumerFactory() {

		Map<String, Object> props = new HashMap<>();

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "delivery-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		// 지금부터 들어오는 값부터 받을 것이다.
		// 처음부터 메시지를 읽지 않는다.
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

		JsonDeserializer<PaymentCompletedEvent> deserializer = new JsonDeserializer<>(PaymentCompletedEvent.class);

		return new DefaultKafkaConsumerFactory<>(
			props,
			new StringDeserializer(),
			deserializer
		);
	}

	// 배송 관련 리스너 컨테이너 팩토리
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> deliveryKafkaListenerContainerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, PaymentCompletedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(deliveryConsumerFactory());

		return factory;
	}
}