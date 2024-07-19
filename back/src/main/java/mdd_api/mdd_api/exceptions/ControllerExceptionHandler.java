package mdd_api.mdd_api.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;

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
	 
	 @ExceptionHandler(MissingServletRequestPartException.class)
	 public ResponseEntity<Map<String, String>> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
	        Map<String, String> response = new HashMap<>();
	        response.put("error", "Un paramètre requis est manquant");
	        response.put("part", ex.getRequestPartName());
	        response.put("message", ex.getMessage());

	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	 }
	 
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ExceptionHandler(value = {HttpMessageNotReadableException.class})
	 public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
	        String errorMessage = "Invalid request body: " + e.getLocalizedMessage();
	    	 
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(errorMessage);        
	 }
	 
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	 public Map<String, Object> handleInvalidArgument(MethodArgumentNotValidException ex) {
	    	
	        Map<String, Object> errorMap = new HashMap<>();
	        
	        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
	            errorMap.put(error.getField(), error.getDefaultMessage());
	        }
	        
	        errorMap.put("status", HttpStatus.BAD_REQUEST);
	        
	        return errorMap;
	 }
	 
	 @ExceptionHandler(CustomException.class) 
     public ResponseEntity<Object> handleCustomException(CustomException ex) {
	        var response = new HashMap<String, Object>();
	        response.put("message", ex.getMessage());
	        response.put("status", ex.getHttpStatus().value());
	        return ResponseEntity
	                .status(ex.getHttpStatus()) 
	                .body(response);
	 }
	 
	 @ExceptionHandler(ExpiredJwtException.class)
	 public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body("Token has expired.");
	 }

	 
	 @ExceptionHandler(Exception.class)
	 public ResponseEntity<Object> handleGenericException(Exception ex) {
	     Map<String, Object> errorMap = new HashMap<>();
	     errorMap.put("message", "Unexpected error occurred: " + ex.getMessage());
	     errorMap.put("status", HttpStatus.BAD_REQUEST.value());
	     
	     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
	 }
	 
	 @ExceptionHandler(AuthenticationException.class)
	 public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
	     Map<String, Object> errorMap = new HashMap<>();
	     errorMap.put("message", "Invalid Credentials: " + ex.getMessage());
	     errorMap.put("status", HttpStatus.BAD_REQUEST.value());
	     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
	 }
	 
	 @ExceptionHandler(EntityNotFoundException.class)
	 public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
         Map<String, Object> errorMap = new HashMap<>();
         errorMap.put("message", ex.getMessage());
	     errorMap.put("status", HttpStatus.NOT_FOUND.value());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }
	 
	 @ExceptionHandler(AccessDeniedException.class)
	 public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
	     Map<String, Object> response = new HashMap<>();
	     response.put("message", "Access denied: " + ex.getMessage());
	     response.put("status", HttpStatus.FORBIDDEN.value());
	     return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	 }
	 
	 
}
