package org.changsol.apps.blogs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.changsol.apps.blogs.dto.KakaoBlogDto;
import org.changsol.apps.property.KakaoProperty;
import org.changsol.utils.WebClientUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoBlogService {

	private final WebClient webClient;
	private final KakaoProperty kakaoProperty;

	/**
	 * 카카오 블로그 검색
	 *
	 * @param request KakaoBlogDto.Request
	 * @return KakaoBlogDto.Response
	 */
	public KakaoBlogDto.Response getBlogPage(KakaoBlogDto.Request request) {
		final String URL = kakaoProperty.getHost() + kakaoProperty.getBlogSearchEndPoint();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
		headers.add("Authorization", "KakaoAK " + kakaoProperty.getRestApiKey());

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		if (StringUtils.isNotBlank(request.getQuery())) {
			paramMap.add("query", request.getQuery());
		}
		if (request.getSort() != null) {
			paramMap.add("sort", request.getSort().name().toLowerCase());
		}
		if (request.getPage() != null) {
			paramMap.add("page", request.getPage().toString());
		}
		if (request.getSize() != null) {
			paramMap.add("size", request.getSize().toString());
		}

		return WebClientUtils.retrieveGetForMono(webClient, URL, headers, paramMap, KakaoBlogDto.Response.class)
							 .block();
	}
}
