package org.example.kafkaredis.domain.productRanking.controller;

import java.util.List;

import org.example.kafkaredis.common.model.redis.RankingDto;
import org.example.kafkaredis.domain.service.ProductRankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * ProductRankingController 클래스입니다.
 * <p>
 * TODO: 클래스의 역할을 작성하세요.
 * </p>
 *
 * @author 재원
 * @version 1.0
 * @since 2026. 1. 8.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking/product")
public class ProductRankingController {

	private final ProductRankingService productRankingService;

	@GetMapping("/today")
	public ResponseEntity<List<RankingDto>> findProductRankingTop3InToday() {
		return ResponseEntity.ok(productRankingService.findProductRankingTop3InToday());
	}

}