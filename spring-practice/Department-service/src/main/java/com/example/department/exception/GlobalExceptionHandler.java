package com.example.department.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	 public ResponseEntity<ErrorResponse>
    handleResourceNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity.status(
                HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        ex.getMessage()));
    }
	
	 @ExceptionHandler(
	            MethodArgumentNotValidException.class)
	    public ResponseEntity<ErrorResponse>
	    handleValidation(
	            MethodArgumentNotValidException ex) {

	        String message =
	                ex.getBindingResult()
	                        .getFieldError()
	                        .getDefaultMessage();

	        return ResponseEntity.badRequest()
	                .body(new ErrorResponse(
	                        LocalDateTime.now(),
	                        message));
	    }
	

}
