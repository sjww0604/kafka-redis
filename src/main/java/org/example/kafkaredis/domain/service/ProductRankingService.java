package org.example.kafkaredis.domain.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.example.kafkaredis.common.model.redis.RankingDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ProductRankingService 클래스입니다.
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
public class ProductRankingService {

	// ProductRankingListener에서 받은 메시지를 기반으로

	// 오늘 가장 많이 판매 된 상품 리스트 랭킹을 만들어 줄 예정

	// Redis의 sorted set을 통해서 만들어 줄 예정이다!

	// StringRedisTemplate을 통해서 redis에 랭킹 데이터를 만들어 줄 예정

	private final StringRedisTemplate stringRedisTemplate;

	public static final String PRODUCT_RANKING_DAILY_KEY = "product:ranking:";

	// 받아온 결제 완료 정보를 기준으로 오늘 판매된 상품 랭킹을 만들어주는 메서드

	public void increaseProductRanking(long productId, LocalDate currentDate)  {


		// 오늘 판매 완료된 상품을 저장할 키 만들어줄 예정
		// 2025-12-25 이다 -> product:ranking:2025-12-25
		// 2025-12-26 이다 -> product:ranking:2025-12-26
		String key = PRODUCT_RANKING_DAILY_KEY + currentDate.toString();

		stringRedisTemplate.opsForZSet().incrementScore(key, String.valueOf(productId), 1);

	}

	// 주문 완료 데이터를 기준으로
	// 결제 완료 API가 호출 될 것
	// 결제가 성공하면 PaymentCompletedEvent 라는 객체로 Kafka에 메시지를 발행햊루 예정
	// Kafka 메시지는 payment-completed라는 토픽으로 저장

	// product-ranking-group 이라는 그룹에서 값을 컨슘해갈 예정
	// 가져온 이벤트에서 언제 결제 완료된 날짜인지, 어떤 상품을 결제 완료했는지 정보를 뽑아서
	// 해당 값을 기준으로 오늘 구매 완료된 상품 랭킹에 반영을 해주는 것

	// 저장된 redis의 오늘 가장 많이 팔린 상품 TOP3 조회하는 내용을 만들어 볼 예정

	public List<RankingDto> findProductRankingTop3InToday() {

		LocalDate currentDate = LocalDate.now();

		String key = PRODUCT_RANKING_DAILY_KEY + currentDate.toString();

		Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet()
			.reverseRangeWithScores(key, 0, 2);

		if (result == null) {
			return Collections.emptyList();
		}

		return result.stream()
			.map(tuple -> new RankingDto(tuple.getValue(), tuple.getScore()))
			.toList();
	}

}