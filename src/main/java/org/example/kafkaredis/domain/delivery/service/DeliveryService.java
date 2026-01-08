package org.example.kafkaredis.domain.delivery.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.example.kafkaredis.common.entity.Delivery;
import org.example.kafkaredis.common.enums.DeliveryStatus;
import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredis.domain.delivery.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DeliveryService 클래스입니다.
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
@Slf4j
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final DeliveryCacheService deliveryCacheService;

	// 리스너에서 받아온 이벤트를 기준으로 배송 데이터 생성해줄 것
	public void createDeliveryFromPayment(PaymentCompletedEvent event) {

		// 해당 이벤트를 기준으로 배송 데이터를 생성해줄 예정

		// 결제 완료된 시간을 String -> localDateTime으로 형변환
		LocalDateTime paidAt = LocalDateTime.parse(
			event.getPaidAt(),
			DateTimeFormatter.ISO_LOCAL_DATE_TIME);

	Delivery delivery = Delivery.builder()
		.orderId(event.getOrderId())
		.paymentId(event.getPaymentId())
		.productId(event.getProductId())
		.userId(event.getUserId())
		.deliveryStatus(DeliveryStatus.PREPARING)
		.statusUpdateAt(paidAt)
		// 실무에서 의외로 수동으로 데이터를 다시 만들어주는 경우가 많다.
		// 결제가 2025-12-07 11시
		// 2025-12-08 11시에 수동으로 만들어 줬다.
		.build();

	deliveryRepository.save(delivery);

	log.info("[Delivery DB] 배송 준비 생성 - orderId : {}, status : {}",
		delivery.getOrderId(), delivery.getDeliveryStatus());

		deliveryCacheService.cacheDelivery(delivery);
	}

}