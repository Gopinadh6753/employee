package com.example.jpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.jpa.model.Department;
import com.example.jpa.repository.DepartmentRepository;

@Service
public class DepartmentService {

	private final  DepartmentRepository repository;

	public DepartmentService(DepartmentRepository repository) {
		this.repository=repository;
	}
	
	public List<Department> getAllDepartments(){
		return repository.findAll();
	}
	
	public Optional<Department> getDeportmentById(int id){
		return repository.findById(id);
	}
	
	public Department saveDepartment(Department dep) {
		return repository.save(dep);
	}
	
	public Department updateDepartment(int id, Department dep) {
		dep.setId(id);
        return repository.save(dep);
    }

    public void deleteDepartment(int id) {
        repository.deleteById(id);
    }
}
