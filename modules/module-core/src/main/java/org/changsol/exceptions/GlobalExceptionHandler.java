package org.changsol.exceptions;

import com.google.common.collect.Lists;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.changsol.apps.logs.service.ErrorLogService;
import org.changsol.exceptions.dto.ExceptionDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

	private final ErrorLogService errorLogService;
	private final HttpServletRequest httpServletRequest;

	/**
	 * Exception Global 처리
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ExceptionDto.Response> handleException(Exception ex) {
		return ResponseEntity.internalServerError()
							 .body(getExceptionResponseAndErrorLogSave(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	/**
	 * BadRequestException Global 처리
	 */
	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity<ExceptionDto.Response> handleException(BadRequestException ex) {
		// return ResponseEntity.badRequest().build();
		return ResponseEntity.badRequest()
							 .body(getExceptionResponseAndErrorLogSave(HttpStatus.BAD_REQUEST, ex));
	}

	/**
	 * BindException Global 처리
	 */
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<ExceptionDto.Response> handleBindException(BindException ex) {
		return ResponseEntity.badRequest()
							 .body(getExceptionResponseAndErrorLogSave(HttpStatus.BAD_REQUEST, ex));
	}

	/**
	 * MethodArgumentNotValidException Global 처리
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ExceptionDto.Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return ResponseEntity.badRequest()
							 .body(getExceptionResponseAndErrorLogSave(HttpStatus.BAD_REQUEST, ex));
	}

	/**
	 * ConstraintViolationException Global 처리
	 */
	@ExceptionHandler(value = {ConstraintViolationException.class})
	protected ResponseEntity<ExceptionDto.Response> handleConstraintViolationException(ConstraintViolationException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
							 .body(getExceptionResponseAndErrorLogSave(HttpStatus.CONFLICT, ex));
	}

	/**
	 * DataIntegrityViolationException Global 처리
	 */
	@ExceptionHandler(value = {DataIntegrityViolationException.class})
	protected ResponseEntity<ExceptionDto.Response> handleConstraintViolationException(DataIntegrityViolationException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
							 .body(getExceptionResponseAndErrorLogSave(HttpStatus.CONFLICT, ex));
	}

	/**
	 * ObjectOptimisticLockingFailureException Global 처리
	 */
	@ExceptionHandler(value = {ObjectOptimisticLockingFailureException.class})
	protected ResponseEntity<ExceptionDto.Response> handleConstraintViolationException(ObjectOptimisticLockingFailureException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
							 .body(getExceptionResponseAndErrorLogSave(HttpStatus.CONFLICT, ex));
	}

	/**
	 * ConstraintViolationException Global 처리
	 */
	@ExceptionHandler(value = {WebClientException.class})
	protected ResponseEntity<ExceptionDto.Response> handleConstraintViolationException(WebClientException ex) {
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
							 .body(getExceptionResponseAndErrorLogSave(HttpStatus.BAD_GATEWAY, ex));
	}


	private ExceptionDto.Response getExceptionResponseAndErrorLogSave(HttpStatus httpStatus, Exception ex) {
		ExceptionDto.Response.ResponseBuilder reponseBuilder = ExceptionDto.Response.builder()
																					.status(httpStatus)
																					.statusCode(httpStatus.value())
																					.errorClass(ex.getClass().getName())
																					.requestUri(httpServletRequest.getRequestURI());

		if (ex instanceof ConstraintViolationException constraintViolationException) {
			List<ExceptionDto.FieldErrorResponse> fieldErrors = Lists.newArrayList();
			for (ConstraintViolation<?> constraintViolation : constraintViolationException.getConstraintViolations()) {
				String field = constraintViolation.getPropertyPath() != null ? constraintViolation.getPropertyPath().toString() : "";
				String defaultMessage = constraintViolation.getMessage();
				fieldErrors.add(ExceptionDto.FieldErrorResponse.builder()
															   .fieldName(field)
															   .errorMessage(defaultMessage)
															   .build());
			}
			reponseBuilder.errorMessage("Data is constraint violation Error");
			reponseBuilder.fieldErrors(fieldErrors);
		} else if (ex instanceof BindException bindException) {
			List<ExceptionDto.FieldErrorResponse> fieldErrors = Lists.newArrayList();
			for (FieldError fieldError : bindException.getFieldErrors()) {
				String field = fieldError.getField();
				String defaultMessage = fieldError.getDefaultMessage();
				fieldErrors.add(ExceptionDto.FieldErrorResponse.builder()
															   .fieldName(field)
															   .errorMessage(defaultMessage)
															   .build());
			}
			reponseBuilder.errorMessage("Binding Error");
			reponseBuilder.fieldErrors(fieldErrors);
		} else {
			reponseBuilder.errorMessage(StringUtils.isBlank(ex.getMessage()) ? ex.toString() : ex.getMessage());
		}
		errorLogService.save(httpStatus, ex);
		ex.printStackTrace();
		return reponseBuilder.build();
	}
}
