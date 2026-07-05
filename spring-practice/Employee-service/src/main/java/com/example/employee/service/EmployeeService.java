package com.example.employee.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.employee.dto.DepartmentDto;
import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.entity.Employee;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.repo.EmployeeRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    @CircuitBreaker(name = "departmentService", fallbackMethod = "departmentFallback")
    public EmployeeResponseDto saveEmployee(EmployeeRequestDto dto,
                                            String token) {

        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setDepartmentIds(dto.getDepartmentIds());

        Employee saved = repository.save(employee);

        EmployeeResponseDto response = map(saved);

        List<DepartmentDto> departments = dto.getDepartmentIds()
                .stream()
                .map(id -> getDepartment(id, token))
                .collect(Collectors.toList());

        response.setDepartments(departments);

        return response;
    }

    @CircuitBreaker(name = "departmentService",
            fallbackMethod = "departmentFallbackById")
    public EmployeeResponseDto getEmployeeById(Long id,
                                               String token) {

        Employee employee = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        EmployeeResponseDto response = map(employee);

        List<DepartmentDto> departments = employee.getDepartmentIds()
                .stream()
                .map(departmentId -> getDepartment(departmentId, token))
                .collect(Collectors.toList());

        response.setDepartments(departments);

        return response;
    }

    // Helper method to call another service and get the result using jwt authenticatication. 
    private DepartmentDto getDepartment(Long departmentId,
                                        String token) {

        try {
        	//Getting the jwt token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<Void> entity =
                    new HttpEntity<>(headers);

            ResponseEntity<DepartmentDto> response =
                    restTemplate.exchange(
                            "http://DEPARTMENT-SERVICE/departments/" + departmentId,
                            HttpMethod.GET,
                            entity,
                            DepartmentDto.class);

            return response.getBody();

        } catch (HttpClientErrorException.NotFound ex) {

            DepartmentDto dto = new DepartmentDto();
            dto.setId(departmentId);
            dto.setName("Unavailable");

            return dto;
        }
    }

    public EmployeeResponseDto departmentFallback(
            EmployeeRequestDto dto,
            String token,
            Exception ex) {

        EmployeeResponseDto response = new EmployeeResponseDto();

        response.setName(dto.getName());

        List<DepartmentDto> list = dto.getDepartmentIds()
                .stream()
                .map(id -> {
                    DepartmentDto d = new DepartmentDto();
                    d.setId(id);
                    d.setName("Department Service Unavailable");
                    return d;
                }).collect(Collectors.toList());

        response.setDepartments(list);

        return response;
    }

    public EmployeeResponseDto departmentFallbackById(
            Long id,
            String token,
            Exception ex) {

        Employee employee = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        EmployeeResponseDto response = map(employee);

        DepartmentDto department = new DepartmentDto();
        department.setId(0L);
        department.setName("Department Service Unavailable");

        response.setDepartments(List.of(department));

        return response;
    }

    public String deleteEmployee(Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        repository.delete(employee);

        return "Deleted";
    }

    public List<EmployeeResponseDto> getEmployeeByDeptId(Long id,
                                                         String token) {

        return repository.getEmployeeByDepartmentId(id)
                .stream()
                .map(emp -> getEmployeeById(emp.getId(), token))
                .collect(Collectors.toList());
    }

    private EmployeeResponseDto map(Employee employee) {

        EmployeeResponseDto dto = new EmployeeResponseDto();

        dto.setId(employee.getId());
        dto.setName(employee.getName());

        return dto;
    }
}