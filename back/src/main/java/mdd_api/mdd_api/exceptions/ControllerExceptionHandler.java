package mdd_api.mdd_api.exceptions;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Order(2)
@RestControllerAdvice
public class ControllerExceptionHandler {
	
	 @ExceptionHandler({ SignatureException.class, MalformedJwtException.class })
	 public ResponseEntity<String> handleSignatureException(Exception ex) {
		 
		 ex.printStackTrace();
		 
		 return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                        .body("Invalid JWT Token");
	 }
	 
	 @ExceptionHandler(UsernameNotFoundException.class)
	    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body("User not found: " + ex.getMessage());
	}

}
