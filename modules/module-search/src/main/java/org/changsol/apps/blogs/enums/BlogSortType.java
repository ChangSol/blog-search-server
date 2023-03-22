package org.changsol.apps.blogs.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "블로그 검색 정렬 방식 BlogSortType")
public enum BlogSortType {
	ACCURACY, // 정확도순
	NEW // 최신순
}
