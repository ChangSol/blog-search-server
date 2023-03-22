package org.changsol.apps.blogs.repository;

import java.util.List;
import org.changsol.apps.blogs.domain.BlogSearchKeyword;
import org.changsol.utils.bases.repository.ChangSolBaseRepository;
import org.springframework.stereotype.Repository;

/**
 * BlogSearchKeyword Repository Class
 */
@Repository
public interface BlogSearchKeywordRepository extends ChangSolBaseRepository<BlogSearchKeyword, String> {
	List<BlogSearchKeyword> findTop10ByOrderByCountDesc();
	List<BlogSearchKeyword> findTop10ByKeywordContainingIgnoreCaseOrderByCountDesc(String keyword);
}
