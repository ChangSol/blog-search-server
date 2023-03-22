package org.changsol.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러 DTO
 */
public class ExceptionDto {

	/**
	 * 에러 Response
	 */
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {

		/**
		 * 오류 발생일시
		 */
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
		@Builder.Default
		private LocalDateTime timestamp = LocalDateTime.now();

		/**
		 * 오류 상태
		 */
		@Builder.Default
		private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		/**
		 * 오류 상태코드
		 */
		@Builder.Default
		private Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

		/**
		 * 오류 클래스명
		 */
		private String errorClass;

		/**
		 * 오류 메시지
		 */
		private String errorMessage;

		/**
		 * 요청 경로
		 */
		private String requestUri;

		/**
		 * 유효성 실패 필드 목록
		 */
		@Builder.Default
		private List<FieldErrorResponse> fieldErrors = Lists.newArrayList();
	}

	/**
	 * Field 에러 Reponse
	 */
	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FieldErrorResponse {

		/**
		 * 유효성 실패 필드명
		 */
		private String fieldName;

		/**
		 * 유효성 실패 메시지
		 */
		private String errorMessage;
	}

}
