package com.example.demo.dto;



public class ReviewRequest {

   
    private String code;
    private String language;

//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
    

    public String getCode() {
        return code;
    }

    public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setCode(String code) {
        this.code = code;
    }
}
