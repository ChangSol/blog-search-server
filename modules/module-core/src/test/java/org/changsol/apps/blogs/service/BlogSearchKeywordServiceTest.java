package org.changsol.apps.blogs.service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.changsol.apps.blogs.domain.BlogSearchKeyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogSearchKeywordServiceTest {

	@Autowired
	private BlogSearchKeywordService blogSearchKeywordService;

	@Nested
	@DisplayName("인기검색어 조회 테스트")
	class findTop10ByKeywordTest {

		@Test
		@DisplayName("키워드 없는 경우")
		void findTop10ByNotKeyword() {
			// given
			final String KEYWORD = "인기검색어";
			blogSearchKeywordService.saveKeyword(KEYWORD);

			// when
			List<BlogSearchKeyword> blogSearchKeywords = blogSearchKeywordService.findTop10ByKeyword(null);

			// then
			Assertions.assertThat(blogSearchKeywords.size()).isGreaterThan(0);
		}

		@Test
		@DisplayName("키워드 있는 경우")
		void findTop10ByKeyword() {
			// given
			final String KEYWORD = "인기검색어";
			blogSearchKeywordService.saveKeyword(KEYWORD);

			// when
			List<BlogSearchKeyword> blogSearchKeywords = blogSearchKeywordService.findTop10ByKeyword(KEYWORD);

			// then
			Assertions.assertThat(blogSearchKeywords.size()).isGreaterThan(0);
		}
	}

	@Nested
	@DisplayName("검색어 저장 테스트")
	class saveKeywordTest {

		@Test
		@Transactional
		@DisplayName("저장 테스트")
		void saveKeyword() {
			// given
			final String KEYWORD = "인기검색어";

			// when
			blogSearchKeywordService.saveKeyword(KEYWORD);
		}

		@Test
		@Transactional
		@DisplayName("동시성 저장 테스트")
		void saveKeywordConcurrency() throws InterruptedException {
			final int COUNT = 100;
			String keyword = "백번";
			CountDownLatch latch = new CountDownLatch(COUNT);
			for (int i = 0; i < 10; i++) {
				new Thread(() -> {
					for (int j = 0; j < 10; j++) {
						blogSearchKeywordService.saveKeyword(keyword);
						latch.countDown();
					}
				}).start();
			}
			latch.await();
			Assertions.assertThat(blogSearchKeywordService.findTop10ByKeyword("백번")
														  .stream()
														  .findAny()
														  .orElseThrow(() -> new NullPointerException("데이터 없음"))
														  .getCount()).isEqualTo(COUNT);
		}
	}
}