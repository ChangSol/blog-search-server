package org.changsol.apps.logs.service;

import lombok.RequiredArgsConstructor;
import org.changsol.apps.logs.domain.ErrorLog;
import org.changsol.apps.logs.repository.ErrorLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public class ErrorLogService {

	private final ErrorLogRepository errorLogRepository;

	/**
	 * Error Log 생성
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(Exception ex) {
		errorLogRepository.save(new ErrorLog(ex));
	}
}
