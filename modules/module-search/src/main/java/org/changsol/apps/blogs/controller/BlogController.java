package org.changsol.apps.blogs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.changsol.apps.blogs.dto.BlogDto;
import org.changsol.apps.blogs.service.BlogService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "블로그 검색 서비스", description = "블로그 검색 서비스입니다.") // Swagger API 명 설정
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/blogs")
public class BlogController {

	private final BlogService blogService;

	@Operation(summary = "블로그 검색", description = "블로그 검색 API입니다.")
	@GetMapping
	public ResponseEntity<BlogDto.Response> getBlogPage(@ParameterObject @Valid BlogDto.Request request) {
		return ResponseEntity.ok(blogService.getBlogPage(request));
	}

	@Operation(summary = "블로그 인기검색어", description = "블로그 인기검색어를 조회하는 API입니다. 최대 10건 제공")
	@GetMapping("/keyword/top")
	public ResponseEntity<List<BlogDto.KeywordTopResponse>> getKeywordTops(@Parameter(description = "검색 키워드")
																		   @RequestParam(required = false) String keyword) {
		return ResponseEntity.ok(blogService.getKeywordTops(keyword));
	}
}
