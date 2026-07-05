package com.example.employee.dto;

public class DepartmentDto {

    private Long id;
    private String name;
	public DepartmentDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DepartmentDto(Long id, String name) {
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

    // getters setters
    
}
