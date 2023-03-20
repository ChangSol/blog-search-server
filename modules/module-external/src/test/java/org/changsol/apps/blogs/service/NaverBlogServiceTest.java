package org.changsol.apps.blogs.service;

import org.changsol.apps.blogs.dto.KakaoBlogDto;
import org.changsol.apps.blogs.dto.NaverBlogDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NaverBlogServiceTest {

	@Autowired
	private NaverBlogService naverBlogService;

	@Test
	void getBlogPostPage() {
		var d = naverBlogService.getBlogPage(NaverBlogDto.Request.builder()
																 .query("집짓기")
																 .build());
	}
}