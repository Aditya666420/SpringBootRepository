package com.example.department.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.department.dto.DepartmentDto;
import com.example.department.dto.DepartmentResponse;
import com.example.department.entity.Department;
import com.example.department.service.DepartmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(
            DepartmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Department>
    createDepartment(
            @Valid
            @RequestBody DepartmentDto dto) {

        return ResponseEntity.status(
                HttpStatus.CREATED)
                .body(service.saveDepartment(dto));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>>
    getAllDepartments() {

        return ResponseEntity.ok(
                service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse>
    getDepartmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getDeptById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department>
    updateDepartment(
            @PathVariable Long id,
            @Valid
            @RequestBody DepartmentDto dto) {

//        return ResponseEntity.ok(
//                service.updateDept(id, dto));
    	return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.updateDept(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteDepartment(
            @PathVariable Long id) {

        service.deleteDept(id);

        return ResponseEntity.ok(
                "Department deleted successfully");
    }
}


