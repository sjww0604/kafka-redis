package org.example.kafkaredis.common.entity;

import java.time.LocalDateTime;

import org.example.kafkaredis.common.enums.DeliveryStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Delivery 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@Entity
@Table(name = "deliveries")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long orderId;
	private Long paymentId;
	private Long productId;
	private Long userId;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus deliveryStatus;

	private LocalDateTime paidAt; //결제완료시간
	private LocalDateTime statusUpdateAt; // 마지막 배송 상태 업데이트 시간

	// 배송 시작 (준비 -> 배송중)
	public void markShipping(LocalDateTime now) {
		this.deliveryStatus = DeliveryStatus.SHIPPING;
		this.statusUpdateAt = now;
	}

	// 배송 완료 (배송중 -> 완료)
	public void markCompleted(LocalDateTime now) {
		this.deliveryStatus = DeliveryStatus.COMPLETED;
		this.statusUpdateAt = now;
	}
}