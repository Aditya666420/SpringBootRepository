package com.example.employee.dto;

import java.util.List;

public class EmployeeResponseDto {

    private Long id;

    private String name;

   

    private List<DepartmentDto> departments;



	public EmployeeResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}



	public EmployeeResponseDto(Long id, String name, List<DepartmentDto> departments) {
		super();
		this.id = id;
		this.name = name;
		this.departments = departments;
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



	public List<DepartmentDto> getDepartments() {
		return departments;
	}



	public void setDepartments(List<DepartmentDto> departments) {
		this.departments = departments;
	}



	
    

	
}
