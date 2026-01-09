package org.example.kafkaredis.domain.productRanking.listener;

import static org.example.kafkaredis.common.model.kafka.topic.KafkaTopic.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredis.domain.service.ProductRankingService;
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

	private final ProductRankingService productRankingService;

	// 아까 등록한 ConsumerFactory, ListenerContainerFactory를 통해서
	// Kafka에 있는 데이터를 가져올 것입니다.
	@KafkaListener(
		topics = TOPIC_PAYMENT_COMPLETED,
		groupId = "product-ranking-group",
		concurrency = "3",
		containerFactory = "productRankingKafkaListenerContainerFactory"
	)
	public void consumer(PaymentCompletedEvent event) {

		log.info("[상품 랭킹 조회 리스너] : 성공적으로 값을 잘 가져왔습니다!");

		// 결제 완료된 시간을 기준으로 파냄 완료 랭킹을 반영해줄 예정입니다.

		// 결제 완료된 시간을 String -> LocalDateTime으로 먼저 받고
		// LocalDateTime -> LocalDate로 받아서 처리할 예정
		LocalDateTime paidAt = LocalDateTime.parse(event.getPaidAt());
		LocalDate currentDate = paidAt.toLocalDate();

		productRankingService.increaseProductRanking(event.getProductId(), currentDate);
	}

}