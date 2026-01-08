package org.example.kafkaredis.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.example.kafkaredis.common.entity.Delivery;
import org.example.kafkaredis.common.enums.DeliveryStatus;
import org.example.kafkaredis.domain.delivery.repository.DeliveryRepository;
import org.example.kafkaredis.domain.delivery.service.DeliveryCacheService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DeliveryStatusScheduler 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryStatusScheduler {

	private final DeliveryRepository deliveryRepository;
	private final DeliveryCacheService deliveryCacheService;

	// 5초에 한번씩 해당 메서드 실행해 주세요.
	@Scheduled(fixedDelay = 15000)
	@Transactional
	// 배송 준비중이었던 데이터를 배송 중으로 변경해라
	public void updatePreparingToShipping() {

		// 10초 지난 값들만 바꿔줄 예정입니다.
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime threshold = now.minusSeconds(10); // 현재 시간 기준으로 10초 전에 상태가 변경된 것들을 찾기 위한 시간

		List<Delivery> preparingList =
			deliveryRepository.findByDeliveryStatusAndStatusUpdateAtBefore(
				DeliveryStatus.PREPARING,
				threshold
			);

		if (preparingList.isEmpty()) {
			return;
		}

		log.info("[Scheduler] : PREPARING TO SHIPPING 전환 대상 : {}건", preparingList.size());

		for (Delivery delivery : preparingList) {
			delivery.markShipping(now);
			deliveryRepository.save(delivery);
			deliveryCacheService.cacheDelivery(delivery);
		}
	}

	// 5초에 한번씩 해당 메서드 실행해 주세요.
	@Scheduled(fixedDelay = 15000)
	@Transactional
	// 배송중이었던 데이터를 배송 완료로으로 변경해라
	public void updateShippingToCompleted() {

		// 10초 지난 값들만 바꿔줄 예정입니다.
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime threshold = now.minusSeconds(10); // 현재 시간 기준으로 10초 전에 상태가 변경된 것들을 찾기 위한 시간

		List<Delivery> shippingList =
			deliveryRepository.findByDeliveryStatusAndStatusUpdateAtBefore(
				DeliveryStatus.SHIPPING,
				threshold
			);

		if (shippingList.isEmpty()) {
			return;
		}

		log.info("[Scheduler] : PREPARING TO COMPLETED 전환 대상 : {}건", shippingList.size());

		for (Delivery delivery : shippingList) {
			delivery.markCompleted(now);
			deliveryRepository.save(delivery);
			deliveryCacheService.cacheDelivery(delivery);
		}
	}

}