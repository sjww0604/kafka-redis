package org.example.kafkaredis.domain.delivery.listener;

import static org.example.kafkaredis.common.model.kafka.topic.KafkaTopic.*;

import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredis.domain.delivery.service.DeliveryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DeliveryListener 클래스입니다.
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
public class DeliveryListener {

	private final DeliveryService deliveryService;

	// 결제 완료 토픽 구독 -> 데이터 받아오기
	// 받아온 데이터는 배송 전용 기능으로 사용할 것이다.
	@KafkaListener(
		topics = TOPIC_PAYMENT_COMPLETED,
		groupId = "delivery-group",
		containerFactory = "deliveryKafkaListenerContainerFactory"
	)
	public void consume(PaymentCompletedEvent event) {
		log.info("[Delivery-Consumer] 결제 완료 이베늩 수신 성공 : orderId : {}, userId : {} ",
			event.getOrderId(), event.getUserId());

		deliveryService.createDeliveryFromPayment(event);

	}

	// 결제 완료 이벤트가 발생하면!
	// 배송을 바로 시작할 것이다!
	// 배송준비 -> 배송중 -> 배송완료
	// 결제 완료 이벤트를 수신하면 컨슘하면 -> 배송준비 상태인 배송데이터를 만들어줄 예정
	// 만든 데이터를 이제 DB에 저장할 것 Delivery라는 엔티티를 통해서 저장할 예정


}