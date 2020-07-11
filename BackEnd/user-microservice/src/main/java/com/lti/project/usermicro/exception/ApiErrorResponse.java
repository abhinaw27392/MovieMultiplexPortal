package com.lti.project.usermicro.exception;

public class ApiErrorResponse {

	private int status;
	private String message;

	public ApiErrorResponse() {
		super();
	}

	public ApiErrorResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return "ErrorResponse { " + " status = " + status + " message = " + message + "}";
	}

}
