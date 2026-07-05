package com.example.employee.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeRequestDto {

    @NotBlank(message = "Employee name is required")
    private String name;

    @NotNull(message = "Department Id is required")
    private List<Long> departmentIds;

	public EmployeeRequestDto(@NotBlank(message = "Employee name is required") String name,
			@NotNull(message = "Department Id is required") List<Long> departmentIds) {
		super();
		this.name = name;
		this.departmentIds = departmentIds;
	}

	public EmployeeRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(List<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}
	
	
   
    
}
