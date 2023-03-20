package org.changsol.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WebClientException extends RuntimeException {

	private final String message;

	public WebClientException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
