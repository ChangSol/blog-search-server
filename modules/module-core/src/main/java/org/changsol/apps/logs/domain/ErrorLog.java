package org.changsol.apps.logs.domain;

import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.changsol.utils.bases.domain.ChangSolBaseDomainIdentity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ErrorLog extends ChangSolBaseDomainIdentity {

	/**
	 * 오류 상태
	 */
	@Builder.Default
	private String status = HttpStatus.INTERNAL_SERVER_ERROR.name();

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
	@Lob
	private String errorMessage;

	private String remoteAddr;

	private String userAgent;

	@Lob
	private String origin;

	@Lob
	private String requestUri;

	private String method;

	/**
	 * 오류 요청 데이터
	 */
	@Lob
	private String data;

	/**
	 * 오류 요청 query
	 */
	@Lob
	private String query;

	@PrePersist
	public void prePersist() {
		Optional.ofNullable(RequestContextHolder.getRequestAttributes())
				.filter(ServletRequestAttributes.class::isInstance)
				.map(ServletRequestAttributes.class::cast)
				.map(ServletRequestAttributes::getRequest)
				.ifPresent(httpServletRequest -> {
					this.remoteAddr = httpServletRequest.getRemoteAddr();
					this.userAgent = httpServletRequest.getHeader(HttpHeaders.USER_AGENT);
					this.origin = httpServletRequest.getHeader(HttpHeaders.ORIGIN);
					this.requestUri = httpServletRequest.getRequestURI();
					this.method = httpServletRequest.getMethod();
					this.query = httpServletRequest.getQueryString();

					try {
						if (httpServletRequest.getReader() != null) {
							this.data = IOUtils.toString(httpServletRequest.getReader());
						}
					} catch (Exception ignore) {
					}
				});
	}
}
