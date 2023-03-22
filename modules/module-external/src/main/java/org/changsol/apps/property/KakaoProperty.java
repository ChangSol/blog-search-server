package org.changsol.apps.property;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 카카오 필요 속성
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakao")
@Validated
public class KakaoProperty {

	@NotBlank
	private String host;
	@NotBlank
	private String blogSearchEndPoint;
	@NotBlank
	private String restApiKey;
}
