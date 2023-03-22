package org.changsol.apps.blogs.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.changsol.apps.blogs.domain.BlogSearchKeyword;
import org.changsol.apps.blogs.repository.BlogSearchKeywordRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogSearchKeywordService {

	private final BlogSearchKeywordRepository blogSearchKeywordRepository;

	/**
	 * 블로그 인기 검색어 TOP 10 조회
	 * @param keyword 키워드 없는 경우 null이나 빈값
	 * @return List<BlogSearchKeyword>
	 */
	public List<BlogSearchKeyword> findTop10ByKeyword(String keyword) {
		if (StringUtils.isBlank(keyword)) {
			return blogSearchKeywordRepository.findTop10ByOrderByCountDesc();
		} else {
			return blogSearchKeywordRepository.findTop10ByKeywordContainingIgnoreCaseOrderByCountDesc(keyword);
		}
	}

	/**
	 * 블로그 검색 키워드 추가 및 갱신
	 * @param keyword 키워드
	 */
	@Retryable(maxAttempts = 10, value = {ObjectOptimisticLockingFailureException.class, DataIntegrityViolationException.class}, backoff = @Backoff(delay = 1000))
	@Transactional
	public void saveKeyword(String keyword) {
		BlogSearchKeyword blogSearchKeyword = blogSearchKeywordRepository.findById(keyword)
																		 .orElse(BlogSearchKeyword.builder()
																								  .keyword(keyword)
																								  .build());
		blogSearchKeyword.countUp();
		blogSearchKeywordRepository.save(blogSearchKeyword);
	}
}
