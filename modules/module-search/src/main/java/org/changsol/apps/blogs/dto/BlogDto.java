package org.changsol.apps.blogs.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.changsol.apps.blogs.enums.BlogSortType;

/**
 * 블로그 검색 DTO
 */
public class BlogDto {

	/**
	 * 블로그 검색 Request
	 */
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(title = "블로그 검색 Request")
	public static class Request {

		/**
		 * 검색 키워드
		 */
		@Parameter(description = "검색 키워드", required = true)
		@Schema(description = "검색 키워드")
		@NotBlank(message = "검색 키워드는 필수입니다.")
		private String keyword;

		/**
		 * 검색 정렬 방식
		 */
		@Parameter(description = "정렬 방식 (ACCURACY:정확도순, NEW:최신순)", example = "ACCURACY", required = true)
		@Schema(description = "정렬 방식 (ACCURACY:정확도순, NEW:최신순)", enumAsRef = true)
		@NotNull(message = "정렬 방식은 필수입니다.")
		private BlogSortType sort;

		/**
		 * 결과 페이지 번호, 1~50 사이의 값
		 */
		@Parameter(description = "검색 페이지 (MIN:1, MAX:50)", example = "1", required = true)
		@Schema(description = "검색 페이지 (MIN:1, MAX:50)")
		@NotNull(message = "검색 페이지는 필수입니다.")
		@Min(value = 1, message = "검색 페이지는 1이상 50이하만 가능합니다.")
		@Max(value = 50, message = "검색 페이지는 1이상 50이하만 가능합니다.")
		private Integer page;

		/**
		 * 한 페이지에 보여질 문서 수, 1~50 사이의 값
		 */
		@Parameter(description = "한 페이지 기준 노출 수 (MIN:1, MAX:50)", example = "10", required = true)
		@Schema(description = "한 페이지 기준 노출 수")
		@NotNull(message = "한 페이지 기준 노출 수는 필수입니다.")
		@Min(value = 1, message = "검색 페이지는 1이상 50이하만 가능합니다.")
		@Max(value = 50, message = "검색 페이지는 1이상 50이하만 가능합니다.")
		private Integer size;
	}

	/**
	 * 블로그 검색 Response
	 */
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(title = "블로그 검색 Response")
	public static class Response {

		/**
		 * 요청한 데이터 정보
		 */
		@Schema(description = "요청한 데이터 정보")
		private Request request;

		/**
		 * 총 검색량
		 */
		@Schema(description = "총 검색량")
		@Builder.Default
		private Integer totalDataCount = 0;

		/**
		 * 총 페이지수
		 */
		@Schema(description = "총 페이지수")
		@Builder.Default
		private Integer totalPageCount = 0;

		/**
		 * 이전 페이지 여부
		 */
		@Schema(description = "이전 페이지 여부")
		@Builder.Default
		private Boolean isPrev = false;

		/**
		 * 다음 페이지 여부
		 */
		@Schema(description = "다음 페이지 여부")
		@Builder.Default
		private Boolean isNext = false;

		/**
		 * 데이터 목록
		 */
		@Schema(description = "데이터 목록")
		@Builder.Default
		private List<DataResponse> datas = Lists.newArrayList();

		/**
		 * 총 페이지 수 유효성 검증 및 갱신
		 */
		public void totalPageCountValidationUpdate() {
			// 총 페이지 수 후가공
			if (!this.isNext && this.totalPageCount > this.request.page) {
				// 마지막 페이지이고 총 페이지수가 요청 페이지보다 클 경우 총 페이지 수를 요청 페이지로 변경.
				this.totalPageCount = this.request.page;
			}
		}
	}

	/**
	 * 블로그 검색 DataResponse
	 */
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(title = "블로그 검색 DataResponse")
	public static class DataResponse {

		/**
		 * 블로그 글 제목
		 */
		@Schema(description = "블로그 제목")
		private String title;

		/**
		 * 블로그 글 요약
		 */
		@Schema(description = "블로그 내용 요약")
		private String contents;

		/**
		 * 블로그 글 URL
		 */
		@Schema(description = "블로그 URL")
		private String url;

		/**
		 * 블로그의 이름
		 */
		@Schema(description = "블로그 명")
		private String blogName;

		/**
		 * 대표 미리보기 이미지 URL
		 */
		@Schema(description = "미리보기 이미지 URL")
		private String thumbnail;

		/**
		 * 블로그 글 작성시간, ISO 8601
		 */
		@Schema(description = "작성일시")
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+9")
		private LocalDateTime datetime;
	}

	/**
	 * 블로그 인기 검색어 KeywordTopResponse
	 */
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(title = "블로그 인기검색어 KeywordTopResponse")
	public static class KeywordTopResponse {

		/**
		 * 검색어
		 */
		@Schema(description = "검색어")
		private String keyword;

		/**
		 * 검색 횟수
		 */
		@Schema(description = "검색 횟수")
		private Long count;
	}

}
