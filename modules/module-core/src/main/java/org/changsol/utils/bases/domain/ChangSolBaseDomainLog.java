package org.changsol.utils.bases.domain;

import java.util.Optional;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
@MappedSuperclass // 자식에게 매핑정보만 제공
public class ChangSolBaseDomainLog extends ChangSolBaseDomainIdentity {

	private String remoteAddr;

	private String userAgent;

	@Lob
	private String origin;

	@Lob
	private String requestUri;

	private String method;

	@PrePersist
	public void prePersist() {
		getHttpServletRequest()
			.ifPresent(httpServletRequest -> {
				this.remoteAddr = httpServletRequest.getRemoteAddr();
				this.userAgent = httpServletRequest.getHeader("User-Agent");
				this.origin = httpServletRequest.getHeader("Origin");
				this.requestUri = httpServletRequest.getRequestURI();
				this.method = httpServletRequest.getMethod();
			});
	}

	protected static Optional<HttpServletRequest> getHttpServletRequest() {
		return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
					   .filter(ServletRequestAttributes.class::isInstance)
					   .map(ServletRequestAttributes.class::cast)
					   .map(ServletRequestAttributes::getRequest);
	}

}
