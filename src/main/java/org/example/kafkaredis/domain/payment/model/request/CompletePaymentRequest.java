package org.example.kafkaredis.domain.payment.model.request;

import org.example.kafkaredis.common.enums.Category;

import lombok.Getter;

/**
 * CompletePaymentRequest 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@Getter
public class CompletePaymentRequest {

	private Long orderId;
	private Long paymentId;
	private Long productId;
	private Long userId;
	private Category category;
	private int quantity;

}