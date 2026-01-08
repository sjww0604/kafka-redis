package org.example.kafkaredis.domain.paymentHistory.listener;

import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredis.domain.paymentHistory.service.PaymentHistoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PaymentHistoryListener 클래스입니다.
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
public class PaymentHistoryListener {

	private final PaymentHistoryService paymentHistoryService;

	// payment-completed 토픽을 구독하는 토픽의 데이터를 읽어오는 리스너를 만들어 줄 예정입니다.

	@KafkaListener(
		topics = "payment-completed",
		groupId = "payment-history-group",
		containerFactory = "paymentHistoryKafkaListenerContainerFactory"
	)
	public void consume(PaymentCompletedEvent event) {

		log.info("[Consumer-History] 결제 완료 이벤트 수신! paymentId : {} productId : {}",
			event.getPaymentId(), event.getProductId());

		paymentHistoryService.savePaymentHistory(event);
	}

	// 주문 완료를 할 것
	// 결제 완료 API를 호출, 결제가 성공했다 가정하고 -> Kafka에게 결제 완료 이벤트를 보내준다.
	// payment-completed 라는 토픽에 paymentCompletedEvent 라는 객체를 넣어준다.

	// Consumer 입장에서 처리하면 되는데
	// consumer 1번 오늘 구매한 상품 랭킹 기능 -> product-ranking-group
	// redis의 zset 랭킹을 만들어준 것

	// consumer 2번 결제 완료 기록을 저장해주는 기능 -> payment-history-group
	// 컨슘한 paymentCompletedEvent를 DB에 저장해주는 기능을 만든 것
	// DB에 잘 저장해주는지 확인해보면 끝



}