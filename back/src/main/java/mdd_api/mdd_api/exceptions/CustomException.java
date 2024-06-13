package mdd_api.mdd_api.exceptions;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class CustomException extends RuntimeException {

	private final String message;
	private final HttpStatus httpStatus;
	
	public CustomException(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}
	
	 public HttpStatus getHttpStatus() {
	        return httpStatus;
	 }
	 
	 public String getMessage() {
	        return this.message;
	 }
	
}
