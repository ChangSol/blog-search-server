package org.changsol.apps.blogs.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.changsol.apps.blogs.enums.KakaoBlogSortType;

/**
 * 카카오 블로그 검색 DTO
 */
public class KakaoBlogDto {

	/**
	 * 카카오 블로그 검색 Request
	 */
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Request {

		/**
		 * 검색을 원하는 질의어(특정 블로그 글만 검색하고 싶은 경우, 블로그 url과 검색어를 공백(' ') 구분자로 넣을 수 있음)
		 */
		@NotBlank
		private String query;
		/**
		 * 결과 문서 정렬 방식, ACCURACY(정확도순) 또는 RECENCY(최신순), 기본 값 ACCURACY
		 */
		@NotNull
		private KakaoBlogSortType sort;
		/**
		 * 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
		 */
		@NotNull
		@Size(min = 1, max = 50)
		private Integer page;
		/**
		 * 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10
		 */
		@NotNull
		@Size(min = 1, max = 50)
		private Integer size;
	}

	/**
	 * 카카오 블로그 검색 Response
	 */
	@Getter
	@Setter
	public static class Response {

		private MetaResponse meta;
		private List<DocumentResponse> documents = Lists.newArrayList();
	}

	/**
	 * 카카오 블로그 검색 MetaResponse
	 */
	@Getter
	@Setter
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class MetaResponse {

		/**
		 * 검색된 문서 수
		 */
		private Integer totalCount;

		/**
		 * totalCount 중 노출 가능 문서 수
		 */
		private Integer pageableCount;

		/**
		 * 현재 페이지가 마지막 페이지인지 여부
		 */
		private Boolean isEnd;
	}

	/**
	 * 카카오 블로그 검색 DocumentResponse
	 */
	@Getter
	@Setter
	public static class DocumentResponse {

		/**
		 * 블로그 글 제목
		 */
		private String title;

		/**
		 * 블로그 글 요약
		 */
		private String contents;

		/**
		 * 블로그 글 URL
		 */
		private String url;

		/**
		 * 블로그의 이름
		 */
		private String blogname;

		/**
		 * 대표 미리보기 이미지 URL
		 */
		private String thumbnail;

		/**
		 * 블로그 글 작성시간, ISO 8601
		 */
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
		private LocalDateTime datetime;
	}

}
