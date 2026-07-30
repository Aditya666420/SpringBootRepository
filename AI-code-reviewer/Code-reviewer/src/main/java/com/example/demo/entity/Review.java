package com.example.demo.entity;




import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reviews")

public class Review {

    @Id
    private String id;
    
   private String userId;

    private String code;
    
    private String language;

    private String reviewResult;

    private LocalDateTime createdAt;

	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Review(String id,  String code, String language,String reviewResult, LocalDateTime createdAt,String userId) {
		super();
		this.id = id;
		
		this.code = code;
		this.language=language;
		this.reviewResult = reviewResult;
		this.createdAt = createdAt;
		this.userId = userId;
	}
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(String reviewResult) {
		this.reviewResult = reviewResult;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Review [id=" + id +  ", code=" + code + ", reviewResult=" + reviewResult
				+ ", createdAt=" + createdAt + "]";
	}
    
    

}