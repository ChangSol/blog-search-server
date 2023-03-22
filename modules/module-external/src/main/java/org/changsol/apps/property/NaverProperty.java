package org.changsol.apps.property;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 네이버 필요 속성
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "naver")
@Validated
public class NaverProperty {

	@NotBlank
	private String host;
	@NotBlank
	private String blogSearchEndPoint;
	@NotBlank
	private String clientId;
	@NotBlank
	private String clientSecret;
}
