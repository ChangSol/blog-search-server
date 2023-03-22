package org.changsol.apps.blogs.mapper;

import java.util.List;
import org.changsol.apps.blogs.domain.BlogSearchKeyword;
import org.changsol.apps.blogs.dto.BlogDto;
import org.changsol.apps.blogs.dto.KakaoBlogDto;
import org.changsol.apps.blogs.dto.NaverBlogDto;
import org.changsol.apps.blogs.enums.BlogSortType;
import org.changsol.apps.blogs.enums.KakaoBlogSortType;
import org.changsol.apps.blogs.enums.NaverBlogSortType;
import org.changsol.exceptions.BadRequestException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BlogMapper {

	BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

	// region REQUEST

	// region KAKAO

	/**
	 * 카카오 Request DTO Mapping
	 */
	@Mapping(source = "keyword", target = "query")
	@Mapping(source = "sort", target = "sort", qualifiedByName = "toKakaoBlogSort")
	KakaoBlogDto.Request toKakaoRequest(BlogDto.Request request);

	/**
	 * 카카오 Request DTO 정렬 Mapping
	 */
	@Named("toKakaoBlogSort")
	default KakaoBlogSortType toKakaoBlogSort(BlogSortType blogSortType) {
		if (blogSortType == null) {
			throw new BadRequestException("BlogSortType is required");
		}

		switch (blogSortType) {
			case ACCURACY -> {
				return KakaoBlogSortType.ACCURACY;
			}
			case NEW -> {
				return KakaoBlogSortType.RECENCY;
			}
			default -> throw new BadRequestException("BlogSortType is not match");
		}
	}
	// endregion

	// region NAVER

	/**
	 * 네이버 Request DTO Mapping
	 */
	@Mapping(source = "keyword", target = "query")
	@Mapping(source = "sort", target = "sort", qualifiedByName = "toNaverBlogSort")
	@Mapping(source = "request", target = "start", qualifiedByName = "toNaverStart")
	@Mapping(source = "size", target = "display")
	NaverBlogDto.Request toNaverRequest(BlogDto.Request request);

	/**
	 * 네이버 Request DTO 정렬 Mapping
	 */
	@Named("toNaverBlogSort")
	default NaverBlogSortType toNaverBlogSort(BlogSortType blogSortType) {
		if (blogSortType == null) {
			throw new BadRequestException("BlogSortType is required");
		}

		switch (blogSortType) {
			case ACCURACY -> {
				return NaverBlogSortType.SIM;
			}
			case NEW -> {
				return NaverBlogSortType.DATE;
			}
			default -> throw new BadRequestException("BlogSortType is not match");
		}
	}

	/**
	 * 네이버 Request DTO start(offset) Mapping
	 */
	@Named("toNaverStart")
	default int toNaverStart(BlogDto.Request request) {
		return (request.getPage() - 1) * request.getSize() + 1;
	}
	// endregion

	// endregion

	// region RESPONSE

	//region 블로그 검색

	// region KAKAO

	/**
	 * 카카오 블로그 Response Mapping
	 */
	@Mappings({
		@Mapping(source = "request", target = "request"),
		@Mapping(source = "response.meta.totalCount", target = "totalDataCount", defaultValue = "0"),
		@Mapping(source = "response.meta.pageableCount", target = "totalPageCount", qualifiedByName = "toTotalDataCount"),
		@Mapping(source = "request.page", target = "isPrev", qualifiedByName = "toIsPrev"),
		@Mapping(source = "response.meta.isEnd", target = "isNext", qualifiedByName = "toIsNext"),
		@Mapping(source = "response.documents", target = "datas", qualifiedByName = "toKakaoDataResponse")
	})
	BlogDto.Response toResponse(BlogDto.Request request, KakaoBlogDto.Response response);

	/**
	 * 카카오 블로그 Response Data Mapping
	 */
	@Named("toKakaoDataResponse")
	@Mapping(source = "blogname", target = "blogName")
	BlogDto.DataResponse toKakaoDataResponse(KakaoBlogDto.DocumentResponse documentResponse);
	// endregion

	// region NAVER

	/**
	 * 네이버 블로그 Response Mapping
	 */
	@Mappings({
		@Mapping(source = "request", target = "request"),
		@Mapping(source = "response.total", target = "totalDataCount", defaultValue = "0"),
		@Mapping(source = "response.pageableCount", target = "totalPageCount", qualifiedByName = "toTotalDataCount"),
		@Mapping(source = "request.page", target = "isPrev", qualifiedByName = "toIsPrev"),
		@Mapping(source = "response.isEnd", target = "isNext", qualifiedByName = "toIsNext"),
		@Mapping(source = "response.items", target = "datas", qualifiedByName = "toNaverDataResponse")
	})
	BlogDto.Response toResponse(BlogDto.Request request, NaverBlogDto.Response response);

	/**
	 * 네이버 블로그 Response Data Mapping
	 */
	@Named("toNaverDataResponse")
	@Mappings({
		@Mapping(source = "description", target = "contents"),
		@Mapping(source = "link", target = "url"),
		@Mapping(source = "bloggername", target = "blogName"),
		@Mapping(source = "postdate", target = "datetime"),
		@Mapping(target = "thumbnail", ignore = true)
	})
	BlogDto.DataResponse toNaverDataResponse(NaverBlogDto.ItemResponse itemResponse);
	// endregion

	// region 공통

	/**
	 * 총 페이지수 MAPPING
	 */
	@Named("toTotalDataCount")
	default int toTotalDataCount(Integer pageableCount) {
		return Math.min(pageableCount == null ? 0 : pageableCount, 50);
	}

	/**
	 * 이전 페이지 여부 MAPPING
	 */
	@Named("toIsPrev")
	default boolean toIsPrev(Integer page) {
		return page != null && page > 1;
	}

	/**
	 * 다음 페이지 여부 MAPPING
	 */
	@Named("toIsNext")
	default boolean toIsNext(Boolean isEnd) {
		return isEnd != null && !isEnd;
	}
	// endregion
	//endregion

	//  region 인기검색어
	List<BlogDto.KeywordTopResponse> toKeywordTopResponse(List<BlogSearchKeyword> blogSearchKeywords);
	// endregion

	// endregion
}
