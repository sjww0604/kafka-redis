package org.example.kafkaredis.domain.delivery.repository;

import java.util.List;

import org.example.kafkaredis.common.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DeliveryRepository 인터페이스입니다.
 * <p>
 * TODO: 인터페이스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

	// 배송 상태를 기준으로 최신 20개 데이터만 가지고 와!
	List<Delivery> findTop20ByUserIdOrderByStatusUpdateAtDesc(Long userId);
}