package org.changsol.apps.blogs.dto;

import com.google.common.collect.Lists;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.changsol.apps.blogs.enums.NaverBlogSortType;

/**
 * 네이버 블로그 검색 DTO
 */
public class NaverBlogDto {

	/**
	 * 네이버 블로그 검색 Request
	 */
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Request {

		/**
		 * 검색어. UTF-8로 인코딩되어야 합니다.
		 */
		private String query;
		/**
		 * 결과 문서 정렬 방식, ACCURACY(정확도순) 또는 RECENCY(최신순), 기본 값 ACCURACY
		 */
		private NaverBlogSortType sort;
		/**
		 * 검색 시작 위치(기본값: 1, 최댓값: 1000)
		 */
		@Size(min = 1, max = 50)
		private Integer start;
		/**
		 * 한 번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
		 */
		@Size(min = 1, max = 100)
		private Integer display;
	}

	/**
	 * 네이버 블로그 검색 Response
	 */
	@Getter
	@Setter
	public static class Response {

		/**
		 * 총 검색 결과 개수
		 */
		private Integer total;

		/**
		 * 총 검색 결과 개수
		 */
		private Integer start;

		/**
		 * 한 번에 표시할 검색 결과 개수
		 */
		private Integer display;

		private List<ItemResponse> items = Lists.newArrayList();

		/**
		 * totalCount 중 노출 가능 문서 수
		 */
		public int getPageableCount() {
			if (this.total == null || display == null || display == 0) {
				return 0;
			} else {
				return (total / display) + (total % display != 0 ? 1 : 0);
			}
		}

		/**
		 * 현재 페이지가 마지막 페이지인지 여부
		 */
		public Boolean getIsEnd() {
			if (this.total == null || start == null || display == null) {
				return false;
			} else {

				return total <= (start * display);
			}
		}
	}

	/**
	 * 네이버 블로그 검색 ItemResponse
	 */
	@Getter
	@Setter
	public static class ItemResponse {

		/**
		 * 블로그 포스트의 제목
		 */
		private String title;

		/**
		 * 블로그 포스트의 URL
		 */
		private String link;

		/**
		 * 블로그 포스트의 내용을 요약한 패시지 정보
		 */
		private String description;

		/**
		 * 블로그의 이름
		 */
		private String bloggername;

		/**
		 * 블로그의 주소
		 */
		private String bloggerlink;

		/**
		 * 블로그 포스트가 작성된 날짜
		 */
		private LocalDateTime postdate;

		/**
		 * 블로그 포스트 작성된 날짜 SET CUSTOM
		 * 네이버는 yyyyMMdd형식이므로 set Method를 가공
		 */
		@SuppressWarnings("unused")
		public void setPostdate(String value) {
			this.postdate = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd"))
									 .atTime(0, 0, 0);
		}
	}

}
