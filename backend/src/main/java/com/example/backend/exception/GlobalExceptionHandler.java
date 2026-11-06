package com.example.backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException exception){
		Map<String,String> error = Map.of("error", "Product Not Found","message", exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(
	        MethodArgumentNotValidException exception) {

	    Map<String, String> errors = new HashMap<>();

	    exception.getBindingResult().getFieldErrors().forEach(error ->
	            errors.put(error.getField(), error.getDefaultMessage()));

	    return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
	}
	
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<Map<String, String>> handelInsufficientStock(InsufficientStockException ex){
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error",ex.getMessage()));
	}
	
	@ExceptionHandler(InvalidOrderException.class)
	public ResponseEntity<Map<String, String>> handleInvalidOrder(InvalidOrderException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",ex.getMessage()));
	}
}
