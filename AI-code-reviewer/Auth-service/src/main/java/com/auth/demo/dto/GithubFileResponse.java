package com.auth.demo.dto;


public class GithubFileResponse {

    private String name;
    private String path;
    private String content;
    private String encoding;
    
	public GithubFileResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GithubFileResponse(String name, String path, String content, String encoding) {
		super();
		this.name = name;
		this.path = path;
		this.content = content;
		this.encoding = encoding;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
    
    
}
