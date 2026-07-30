package com.auth.demo.dto;



public class ReviewRequest {

    private String code;
    private String language;

	public ReviewRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReviewRequest(String code,String language) {
		super();
		this.code = code;
		this.language=language;
	}
	

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
    
}

