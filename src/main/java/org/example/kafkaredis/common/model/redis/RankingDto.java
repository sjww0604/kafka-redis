package org.example.kafkaredis.common.model.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RankingDto 클래스입니다.
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
public class RankingDto {

	private String title;
	private double score;

}