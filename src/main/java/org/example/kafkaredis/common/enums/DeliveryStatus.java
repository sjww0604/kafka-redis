package org.example.kafkaredis.common.enums;

/**
 * DeliveryStatus enum입니다.
 * <p>
 * TODO: enum의 역할과 각 상수의 의미를 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
public enum DeliveryStatus {
	PREPARING,   // 배송 준비
	SHIPPING,    // 배송 시작
	COMPLETED    // 배송 완료
}