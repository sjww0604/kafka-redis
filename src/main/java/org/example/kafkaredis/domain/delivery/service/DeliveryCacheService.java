package org.example.kafkaredis.domain.delivery.service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.example.kafkaredis.common.entity.Delivery;
import org.example.kafkaredis.common.enums.DeliveryStatus;
import org.example.kafkaredis.domain.delivery.model.response.DeliveryResponse;
import org.example.kafkaredis.domain.delivery.repository.DeliveryRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DeliveryCacheService 클래스입니다.
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
public class DeliveryCacheService {

	private final DeliveryRepository deliveryRepository;
	private final StringRedisTemplate stringRedisTemplate;

	// 배송 상태를 DB 뿐만 아니라 캐시에다가도 저장할 예정
	public void cacheDelivery(Delivery delivery) {

		// Redis에 저장할 키를 만들 예정이다.
		// deliveryId 기준으로 어떤 상태값을 가지고 있는지 redis 캐시에 저장하기 위한 목적
		String key = "delivery:status" + delivery.getId();

		// redis에 값을 넣어줄 건데
		stringRedisTemplate.opsForHash().putAll(key, Map.of(
			"status", delivery.getDeliveryStatus().name(), //배송 상태
			"orderId", delivery.getOrderId().toString(), //주문 아이디
			"productId", delivery.getProductId().toString(), // 상품 아이디
			"statusUpdateAt", delivery.getStatusUpdateAt().toString() // 배송 상태가 언제 업데이트 되었는지
		));

		// 이렇게 일단 redis에 배송 ID를 기준으로 값을 넣어줬다.

		// 사용자별 배송 목록 조회 기능을 만들어 줄 예정
		// userId를 기준으로 현재 내가 주문한 상품의 배송 상태를 조회하는 기능
		// 정렬을 배송상태 업데이트 기준으로 정렬을 해라

		// zset, sorted set을 사용해서 구현할 예정
		// key, value, 점수

		// key : user_deliveries:{userId}

		// value : deliveryId를 기준으로 값을 넣어줄 것이다.

		// 유저 아이디 별로 배송 상태 최신화 목록 조회를 만들 것이다.

		// key 셋팅
		String userDeliveryKey = "user_deliveries:" + delivery.getUserId();

		// value 셋팅 delivery.getId();

		// score 셋팅
		// localDateTime을 점수화 한다.
		double score = delivery.getStatusUpdateAt().toEpochSecond(ZoneOffset.UTC);

		// redis에 값을 넣어주면 끝 -> 유저 아이디 별로 배송 상태 최신화 에 값을 넣어준 것
		stringRedisTemplate.opsForZSet()
			.add(userDeliveryKey, delivery.getId().toString(), score);

		log.info("[Delivery-Redis] 캐시에 저장 완료! key : {}, status : {}", key, delivery.getDeliveryStatus());
	}

	// DB가 아니라 캐시 데이터를 기준으로 조회하는 방법
	// 유저별 최신 배송 정보 조회
	public List<DeliveryResponse> findUserDeliveries(long userId) {

		// 값을 조회할 것

		// Key로 어떤 redis 데이터를 조회할 것이냐

		// 유저 아이디 별로 배송 상태 최신화 목록 조회를 만들 것이다.

		// user:deliveries:userId -> 배송 상태 최신화 목록 20개

		String userKey = "user:deliveries" + userId;

		// zset 조회 유저 아이디 별로 배송 상태 최신화 목록 조회
		Set<String> deliveryIdList =
			stringRedisTemplate.opsForZSet().reverseRangeByScore(userKey, 0, 19);

		// 캐시에 저 ID 리스트가 없다면 DB에서 직접 조회를 하면 된다.

		if (deliveryIdList == null || deliveryIdList.isEmpty()) {
			return deliveryRepository.findTop20ByUserIdOrderByStatusUpdateAtDesc(userId)
				.stream()
				.map(delivery -> new DeliveryResponse(
					delivery.getId(),
					delivery.getOrderId(),
					delivery.getProductId(),
					delivery.getDeliveryStatus(),
					delivery.getStatusUpdateAt().toString()
				))
				.toList();
		}

		// 최신화 목록 캐시가 있어! 그러면 DB에서 직접 조회할 필요 없이 캐시에서 필요한 데이터를 가져오면 됨

		List<DeliveryResponse> result = new ArrayList<>();

		for (String deliveryId : deliveryIdList) {

			String key = "delivery_status:" + deliveryId;

			Map<Object, Object> cached = stringRedisTemplate.opsForHash().entries(key);

			// 캐시가 없다면
			if (cached.isEmpty()) {
				//DB에서 직접 조회해서 가져오면 된다!
				Delivery delivery = deliveryRepository.findById(Long.valueOf(deliveryId)).orElseThrow();
				result.add(new DeliveryResponse(
					delivery.getId(),
					delivery.getOrderId(),
					delivery.getProductId(),
					delivery.getDeliveryStatus(),
					delivery.getStatusUpdateAt().toString()
				));
				continue;
			}

			DeliveryResponse response = new DeliveryResponse(
				Long.valueOf(deliveryId),
				Long.valueOf(cached.get("orderId").toString()),
				Long.valueOf(cached.get("productId").toString()),
				DeliveryStatus.valueOf(cached.get("status").toString()),
				cached.get("statusUpdateAt").toString()
			);

			result.add(response);
		}

		return result;
	}
}