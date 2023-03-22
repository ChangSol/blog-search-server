package org.changsol.utils;

import org.apache.commons.collections4.MapUtils;
import org.changsol.exceptions.WebClientException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

public class WebClientUtils {

	public static <T> Mono<T> retrieveGetForMono(WebClient webClient,
												 String url,
												 HttpHeaders httpHeaders,
												 MultiValueMap<String, String> param,
												 Class<T> returnClass) {
		if (httpHeaders == null) {
			httpHeaders = new HttpHeaders();
		}

		if (MapUtils.isEmpty(param)) {
			param = new LinkedMultiValueMap<>();
		}

		final HttpHeaders HEADERS = httpHeaders;
		final MultiValueMap<String, String> PARAM = param;
		final UriComponentsBuilder BUILDER = UriComponentsBuilder.fromHttpUrl(url).queryParams(PARAM);
		return webClient.mutate()
						.build()
						.get()
						.uri(BUILDER.build(false).toUriString())
						.headers(header -> header.addAll(HEADERS))
						.retrieve()
						.onStatus(HttpStatus::is4xxClientError, response -> response.bodyToMono(String.class).map(WebClientException::new))
						.onStatus(HttpStatus::is5xxServerError, response -> response.bodyToMono(String.class).map(RuntimeException::new))
						.bodyToMono(returnClass);
	}
}
