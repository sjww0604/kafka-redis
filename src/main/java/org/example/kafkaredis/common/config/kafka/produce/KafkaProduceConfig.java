package org.example.kafkaredis.common.config.kafka.produce;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * KafkaProduceConfig 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@EnableKafka
@Configuration
public class KafkaProduceConfig {

	// Spring Boot에 Kafka Cluster 주소를 연결
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootStrapServers;

	// ProduceFactory 만들어 줄 예정
	// Spring Boot 에서 발생한 이벤트를 Kafka에 저장시키는 역할을 수행
	@Bean
	public ProducerFactory<String, PaymentCompletedEvent> eventProducerFactory() {

		Map<String, Object> props = new HashMap<>();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory<>(props);
	}

	// KafkaTemplate을 하나 만들어 줄 예정
	// Spring Boot 와 Kafka가 소통할 때 사용하는 객체
	@Bean
	public KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedEventKafkaTemplate() {
		return new KafkaTemplate<>(eventProducerFactory());
	}

}