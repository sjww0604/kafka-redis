package org.example.kafkaredis.common.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.example.kafkaredis.common.enums.Category;
import org.example.kafkaredis.common.model.kafka.event.PaymentCompletedEvent;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PaymentHistory 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@Entity
@Table(name = "payment_histories")
@Getter
@NoArgsConstructor
public class PaymentHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long paymentId;
	private Long orderId;
	private Long productId;
	private Long userId;
	@Enumerated(EnumType.STRING)
	private Category category;
	private int quantity;

	private LocalDateTime paidAt;

	private PaymentHistory(Long paymentId, Long orderId, Long productId, Long userId, Category category, int quantity, LocalDateTime paidAt) {
		this.paymentId = paymentId;
		this.orderId = orderId;
		this.productId = productId;
		this.userId = userId;
		this.category = category;
		this.quantity = quantity;
		this.paidAt = paidAt;
	}

	public static PaymentHistory from(PaymentCompletedEvent event) {
		return new PaymentHistory(
			event.getPaymentId(),
			event.getOrderId(),
			event.getProductId(),
			event.getUserId(),
			event.getCategory(),
			event.getQuantity(),
			LocalDateTime.parse(event.getPaidAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
		);
	}
}