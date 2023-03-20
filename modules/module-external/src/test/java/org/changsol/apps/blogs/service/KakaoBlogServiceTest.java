package org.changsol.apps.blogs.service;

import org.changsol.apps.blogs.dto.KakaoBlogDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KakaoBlogServiceTest {

	@Autowired
	private KakaoBlogService kakaoBlogService;

	@Test
	void getBlogPostPage() {
		kakaoBlogService.getBlogPage(KakaoBlogDto.Request.builder()
														 .query("집짓기")
														 .build());
	}
}