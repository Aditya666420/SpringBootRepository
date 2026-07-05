package com.example.department.dto;

public class DepartmentResponse {
	public Long id;
	public String name;
	public DepartmentResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public DepartmentResponse(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
