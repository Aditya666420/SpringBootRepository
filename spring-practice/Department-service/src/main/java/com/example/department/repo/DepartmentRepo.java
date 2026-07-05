package com.example.department.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.department.entity.Department;

public interface DepartmentRepo extends JpaRepository<Department,Long> {
	
		//Custom sql query
//		@Query(value="Select * FROM Department WHERE department = :deptarment",nativeQuery=true)
//		public Optional<List<Department>> findByDepartmentName(@Param("department") String name);
}
