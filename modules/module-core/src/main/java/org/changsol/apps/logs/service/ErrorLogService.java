package org.changsol.apps.logs.service;

import lombok.RequiredArgsConstructor;
import org.changsol.apps.logs.domain.ErrorLog;
import org.changsol.apps.logs.repository.ErrorLogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ErrorLogService {

	private final ErrorLogRepository errorLogRepository;

	/**
	 * Error Log 생성
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(HttpStatus httpStatus, Exception ex) {
		errorLogRepository.save(ErrorLog.builder()
										.status(httpStatus.name())
										.statusCode(httpStatus.value())
										.errorClass(ex.getClass().getName())
										.errorMessage(ex.toString())
										.build());
	}
}
