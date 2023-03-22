package org.changsol.apps.blogs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.changsol.apps.blogs.dto.NaverBlogDto;
import org.changsol.apps.property.NaverProperty;
import org.changsol.utils.WebClientUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class NaverBlogService {

	private final WebClient webClient;
	private final NaverProperty naverProperty;

	/**
	 * 네이버 블로그 검색
	 *
	 * @param request NaverBlogDto.Request
	 * @return NaverBlogDto.Response
	 */
	public NaverBlogDto.Response getBlogPage(NaverBlogDto.Request request) {
		final String URL = naverProperty.getHost() + naverProperty.getBlogSearchEndPoint();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
		headers.add("X-Naver-Client-Id", naverProperty.getClientId());
		headers.add("X-Naver-Client-Secret", naverProperty.getClientSecret());

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		if (StringUtils.isNotBlank(request.getQuery())) {
			paramMap.add("query", request.getQuery());
		}
		if (request.getSort() != null) {
			paramMap.add("sort", request.getSort().name().toLowerCase());
		}
		if (request.getStart() != null) {
			paramMap.add("start", request.getStart().toString());
		}
		if (request.getDisplay() != null) {
			paramMap.add("display", request.getDisplay().toString());
		}

		return WebClientUtils.retrieveGetForMono(webClient, URL, headers, paramMap, NaverBlogDto.Response.class)
							 .block();
	}
}
