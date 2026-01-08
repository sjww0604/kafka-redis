package org.example.kafkaredis.domain.delivery.model.response;

import org.example.kafkaredis.common.enums.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DeliveryResponse 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@Getter
@AllArgsConstructor
public class DeliveryResponse {

	private Long deliveryId;
	private Long orderId;
	private Long productId;
	private DeliveryStatus deliveryStatus;
	private String statusUpdateAt; // Redis와 통신할 때에는 LocalDateTime을 받기 위해서는 별도의 추가 작업이 필요

}