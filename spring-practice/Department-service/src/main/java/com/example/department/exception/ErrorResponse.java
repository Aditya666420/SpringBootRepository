package com.example.department.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
	
	private LocalDateTime time;
	private String message;
	public ErrorResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ErrorResponse(LocalDateTime time, String message) {
		super();
		this.time = time;
		this.message = message;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
