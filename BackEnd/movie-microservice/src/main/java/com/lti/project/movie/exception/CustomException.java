package com.lti.project.movie.exception;

public class CustomException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;
	
	CustomException() {
		super();
	}
	
	public CustomException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	

}
