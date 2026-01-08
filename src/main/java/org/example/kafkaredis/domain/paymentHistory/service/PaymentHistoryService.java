package org.example.kafkaredis.domain.paymentHistory.service;

import org.example.kafkaredis.common.entity.PaymentHistory;
import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;
import org.example.kafkaredis.domain.paymentHistory.repository.PaymentHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * PaymentHistoryService 클래스입니다.
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
public class PaymentHistoryService {

	private final PaymentHistoryRepository paymentHistoryRepository;

	// 리스너에서 읽어온 값을 DB에 저장하는ㄴ 메서드
	@Transactional
	public void savePaymentHistory(PaymentCompletedEvent event) {

		PaymentHistory paymentHistory = PaymentHistory.from(event);

		paymentHistoryRepository.save(paymentHistory);

		log.info("[DB] : 결제 기록 저장 완료! paymentId : {} , productId : {}",
			paymentHistory.getPaymentId(), paymentHistory.getProductId());
	}

}