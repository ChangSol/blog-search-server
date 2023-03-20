package org.changsol.apps.logs.domain;

import java.io.IOException;
import javax.persistence.Entity;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.changsol.utils.bases.domain.ChangSolBaseDomainLog;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
@Entity
public class ErrorLog extends ChangSolBaseDomainLog {

	/**
	 * 오류 바인딩 함수
	 */
	public ErrorLog(Exception ex) {
		this.message = ex.getMessage();
		this.className = ex.getClass().getName();
		this.detailMessage = ex.toString();

		getHttpServletRequest()
			.ifPresent(httpServletRequest -> {
				this.querystring = httpServletRequest.getQueryString();

				try {
					if (httpServletRequest.getReader() != null) {
						this.data = IOUtils.toString(httpServletRequest.getReader());
					}
				} catch (IOException ex1) {
					log.error("IO Header Error");
				} catch (IllegalStateException ise) {
					log.error("IllegalStateException Error");
				}
			});
	}

	@Lob
	private String message;

	private String className;

	@Lob
	private String detailMessage;

	@Lob
	private String data;

	@Lob
	private String querystring;

}
