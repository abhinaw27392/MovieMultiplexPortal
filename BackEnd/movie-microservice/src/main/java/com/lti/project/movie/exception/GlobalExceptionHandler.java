package com.lti.project.movie.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { IOException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrorResponse badRequest(IOException e) {
		return new ApiErrorResponse(400, e.getMessage());
	}

	@ExceptionHandler(value = { NoHandlerFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrorResponse noHandlerFoundException(NoHandlerFoundException e) {
		return new ApiErrorResponse(404, e.getMessage());
	}

	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiErrorResponse unknownException(Exception e) {
		return new ApiErrorResponse(500, e.getMessage());
	}
}
