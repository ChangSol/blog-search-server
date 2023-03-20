package org.changsol.exceptions;

import com.google.common.collect.Lists;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.changsol.apps.logs.service.ErrorLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

	private final ErrorLogService errorLogService;
	@ExceptionHandler(Exception.class)
	public void handleException(Exception ex) throws Exception {
		ArrayList<Class<? extends Exception>> ignores = Lists.newArrayList(
			AccessDeniedException.class,
			ConstraintViolationException.class,
			MethodArgumentTypeMismatchException.class,
			BadRequestException.class
		);

		if (!ignores.contains(ex.getClass())) {
			errorLogService.save(ex);
		}

		ex.printStackTrace();
		throw ex;
	}

	/**
	 * MethodArgumentNotValidException 에 대한 전역 message 처리
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, Object> errorMap = new LinkedHashMap<>();
		errorMap.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		errorMap.put("status", 400);
		errorMap.put("error", "MethodArgumentNotValidException");
		errorMap.put("message", "Please check your enter");

		Map<String, Object> errorFieldMap = new HashMap<>();
		for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
			String field = ((FieldError) objectError).getField();
			String defaultMessage = objectError.getDefaultMessage();
			errorFieldMap.put(field, defaultMessage);
		}
		errorMap.put("reason", errorFieldMap);
		return ResponseEntity.badRequest().body(errorMap);
	}

	@ExceptionHandler(value = {ConstraintViolationException.class})
	protected ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, Object> errorMap = new LinkedHashMap<>();
		errorMap.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		errorMap.put("status", 400);
		errorMap.put("error", "ConstraintViolationException");
		errorMap.put("message", "Please check your enter");

		Map<String, Object> errorFieldMap = new HashMap<>();
		for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
			String field = constraintViolation.getPropertyPath() != null ? constraintViolation.getPropertyPath().toString() : "";
			String defaultMessage = constraintViolation.getMessage();
			errorFieldMap.put(field, defaultMessage);
		}
		errorMap.put("reason", errorFieldMap);
		return ResponseEntity.badRequest().body(errorMap);
	}
}
