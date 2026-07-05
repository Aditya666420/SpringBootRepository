package com.example.department.service;

import java.util.List;
import java.util.stream.Collectors;
import com.example.department.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.department.dto.DepartmentDto;
import com.example.department.dto.DepartmentResponse;
import com.example.department.entity.Department;
import com.example.department.repo.DepartmentRepo;

@Service
public class DepartmentService {
	
	@Autowired
	DepartmentRepo repo;
	
	@Transactional
	public Department saveDepartment(DepartmentDto d) {
		Department de = new Department();
		de.setName(d.getName());
		return repo.saveAndFlush(de);
		
	}
	
	public List<DepartmentResponse> getAll(){
		List<Department> d = repo.findAll();
		List<DepartmentResponse> dto = d.stream().map(this::mapToResponse).collect(Collectors.toList());
		return dto;
		
	}
	public DepartmentResponse getDeptById(Long id){
		Department d=repo.findById(id).orElseThrow(()->new ResourceNotFoundException(
                "Department not found with id " + id));
		return mapToResponse(d);
		
		
	}
	@Transactional
	public Department  updateDept(Long id,DepartmentDto dto) {
		Department d = repo.findById(id).orElseThrow(()->new ResourceNotFoundException(
                "Department not found with id " + id));
		d.setName(dto.getName());
		return repo.saveAndFlush(d);
		
	}
	@Transactional
	public String  deleteDept(Long id) {
		
		Department d =  repo.findById(id).orElseThrow(()->new ResourceNotFoundException(
                "Department not found with id " + id));
		repo.delete(d);
		return "All department of "+id+" is deleted";
		
	}
	
	
	private DepartmentResponse
    mapToResponse(Department department) {

        return new DepartmentResponse(
               department.getId(),
                department.getName());
    }

}
