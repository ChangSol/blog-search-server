package org.changsol.apps.blogs.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.changsol.apps.blogs.dto.BlogDto;
import org.changsol.apps.blogs.enums.BlogSortType;
import org.changsol.apps.blogs.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BlogController.class)
class BlogControllerTest {

	@MockBean
	private BlogService blogService;

	@Autowired
	MockMvc mockMvc;

	final String BASE_URL = "/v1/blogs";
	final String BASE_KEYWORD = "검색테스트^_^";

	@Autowired
	ObjectMapper objectMapper;
	BlogDto.Request request;

	@BeforeEach
	public void setup() {
		request = BlogDto.Request.builder()
								 .keyword(BASE_KEYWORD)
								 .sort(BlogSortType.ACCURACY)
								 .page(1)
								 .size(1)
								 .build();
	}

	@Test
	@DisplayName("블로그 검색 API 통신 테스트")
	void getBlogPage() throws Exception {
		// given
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		Map<String, String> map = objectMapper.convertValue(request, new TypeReference<>() {
		});
		params.setAll(map);

		// then
		mockMvc.perform(get(BASE_URL).params(params))
			   .andDo(MockMvcResultHandlers.print())
			   .andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@DisplayName("블로그 인기검색어 API 통신 테스트")
	void getKeywordTops() throws Exception {
		// then
		mockMvc.perform(get(BASE_URL + "/keyword/top"))
			   .andDo(MockMvcResultHandlers.print())
			   .andExpect(MockMvcResultMatchers.status().isOk());
	}
}