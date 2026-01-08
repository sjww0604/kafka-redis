package org.example.kafkaredis.domain.payment.controller;

import org.example.kafkaredis.domain.payment.model.request.CompletePaymentRequest;
import org.example.kafkaredis.domain.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * PaymentController 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/completion")
	public ResponseEntity<Void> paymentCompleted(@RequestBody CompletePaymentRequest request) {
		paymentService.paymentComplete(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}