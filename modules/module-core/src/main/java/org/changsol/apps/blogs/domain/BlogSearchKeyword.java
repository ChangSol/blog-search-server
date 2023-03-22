package org.changsol.apps.blogs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.changsol.utils.bases.domain.ChangSolBaseDomain;
import org.hibernate.annotations.ColumnDefault;

/**
 * 블로그 검색 키워드 테이블
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class BlogSearchKeyword extends ChangSolBaseDomain {

	/**
	 * 낙관적 락을 위한 버전 (동시성제어)
	 */
	@Version
	protected Long version;

	/**
	 * 검색 키워드
	 */
	@Id
	private String keyword;

	/**
	 * 검색 횟수
	 */
	@NotNull
	@ColumnDefault("0")
	@Builder.Default
	private Long count = 0L;

	/**
	 * 횟수 증가 함수
	 */
	public void countUp() {
		++this.count;
	}
}
