package org.example.kafkaredis.domain.productRanking.listener;

import static org.example.kafkaredis.common.model.kafka.topic.KafkaTopic.*;

import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ProductRankingListener 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductRankingListener  {

	// 아까 등록한 ConsumerFactory, ListenerContainerFactory를 통해서
	// Kafka에 있는 데이터를 가져올 것입니다.

	@KafkaListener(
		topics = TOPIC_PAYMENT_COMPLETED,
		groupId = "product-ranking-group",
		containerFactory = "productRankingKafkaListenerContainerFactory"
	)
	public void consumer(PaymentCompletedEvent event) {

		log.info("[상품 랭킹 조회 리스너] : 성공적으로 값을 잘 가져왔습니다!");


	}

}