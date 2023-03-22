package org.changsol.apps.blogs.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.changsol.apps.blogs.dto.BlogDto;
import org.changsol.apps.blogs.dto.KakaoBlogDto;
import org.changsol.apps.blogs.dto.NaverBlogDto;
import org.changsol.apps.blogs.mapper.BlogMapper;
import org.changsol.apps.logs.service.ErrorLogService;
import org.changsol.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {

	private final BlogSearchKeywordService blogSearchKeywordService;
	private final KakaoBlogService kakaoBlogService;
	private final NaverBlogService naverBlogService;
	private final ErrorLogService errorLogService;

	/**
	 * 블로그 검색
	 */
	public BlogDto.Response getBlogPage(BlogDto.Request request) {
		BlogDto.Response response = BlogDto.Response.builder()
													.request(request)
													.build();
		KakaoBlogDto.Response kakaoBlogResponse = null;
		NaverBlogDto.Response naverBlogResponse = null;

		try {
			// 카카오 블로그 검색
			kakaoBlogResponse = kakaoBlogService.getBlogPage(BlogMapper.INSTANCE.toKakaoRequest(request));
		} catch (Exception ex) {
			errorLogService.save(HttpStatus.BAD_GATEWAY, ex);
			// 카카오 블로그 Error 시 네이버로 대체
			try {
				// 네이버 블로그 검색
				naverBlogResponse = naverBlogService.getBlogPage(BlogMapper.INSTANCE.toNaverRequest(request));
			} catch (Exception ex2) {
				errorLogService.save(HttpStatus.BAD_GATEWAY, ex2);
			}
		}

		// 블로그 DTO MAPPING
		if (kakaoBlogResponse != null) {
			response = BlogMapper.INSTANCE.toResponse(request, kakaoBlogResponse);
		} else if (naverBlogResponse != null) {
			response = BlogMapper.INSTANCE.toResponse(request, naverBlogResponse);
		}

		if (response != null) {
			// 예외처리
			if (response.getTotalPageCount() < request.getPage()) {
				// 총 페이지수가 요청 페이지보다 작은 경우
				throw new BadRequestException("요청하신 페이지의 데이터가 존재하지 않습니다.");
			}

			// 총 페이지 수 검증
			response.totalPageCountValidationUpdate();

			// 검색어 데이터 추가
			blogSearchKeywordService.saveKeyword(request.getKeyword());
		}

		return response;
	}

	/**
	 * 블로그 인기 검색어 TOP10 조회
	 */
	public List<BlogDto.KeywordTopResponse> getKeywordTops(String keyword) {
		return BlogMapper.INSTANCE.toKeywordTopResponse(blogSearchKeywordService.findTop10ByKeyword(keyword));
	}

}
