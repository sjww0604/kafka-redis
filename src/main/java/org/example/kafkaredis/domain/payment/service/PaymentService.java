package org.example.kafkaredis.domain.payment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredis.domain.payment.model.request.CompletePaymentRequest;
import org.example.kafkaredis.domain.payment.producer.PaymentProducer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * PaymentService 클래스입니다.
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
public class PaymentService {

	private final PaymentProducer producer;

	public void paymentComplete(CompletePaymentRequest request) {

		// 결제 PG 사 통신 로직
		// 실제 결제 시도 로직 들어가는 가리
		// 이번 실습에서는 실제 결제하는 로직은 제외하고 결제를 성공했다고 가정하고 진행하겠습니다.

		// 결제 시도 로직

		// 결제가 최종적으로 성공했으면 해당 메시지 발행

		// 실제로 결제가 성공한 시간
		// 파싱 에러를 막기 위한 포멧팅
		String paidAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

		PaymentCompletedEvent event = PaymentCompletedEvent.builder()
			.paymentId(request.getPaymentId())
			.orderId(request.getOrderId())
			.productId(request.getProductId())
			.userId(request.getUserId())
			.category(request.getCategory())
			.quantity(request.getQuantity())
			.paidAt(paidAt)
			.build();


		producer.send(event);
	}
}