package org.example.kafkaredis.domain.paymentHistory.repository;

import org.example.kafkaredis.common.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PaymentHistoryRepository 인터페이스입니다.
 * <p>
 * TODO: 인터페이스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

}