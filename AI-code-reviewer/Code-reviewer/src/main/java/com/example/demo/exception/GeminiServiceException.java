package com.example.demo.exception;


public class GeminiServiceException
        extends RuntimeException {

    /**
	     * 
	     */
	    private static final long serialVersionUID = 1L;

	public GeminiServiceException(String message) {
        super(message);
    }
}
