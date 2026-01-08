package org.example.kafkaredis.domain.delivery.controller;

import java.util.List;

import org.example.kafkaredis.domain.delivery.model.response.DeliveryResponse;
import org.example.kafkaredis.domain.delivery.service.DeliveryCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * DeliveryController 클래스입니다.
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
@RequestMapping("/api/delivery")
public class DeliveryController {

	private final DeliveryCacheService deliveryCacheService;

	@GetMapping
	public ResponseEntity<List<DeliveryResponse>> findUserDeliveries(@RequestParam Long userId) {
		return ResponseEntity.ok(deliveryCacheService.findUserDeliveries(userId));
	}

}