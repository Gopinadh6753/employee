package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.model.Department;

public interface DepartmentRepository extends JpaRepository<Department,Integer>{

}
