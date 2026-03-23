package com.example.jpa.service;

import com.example.jpa.model.Employee;
import com.example.jpa.repository.EmployeeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Optional<Employee> getEmployeeById(int id) {
        return repository.findById(id);
    }

    public Employee saveEmployee(Employee emp) {
        return repository.save(emp);
    }

    public Employee updateEmployee(int id, Employee emp) {
        emp.setId(id);
        return repository.save(emp);
    }

    public void deleteEmployee(int id) {
        repository.deleteById(id);
    }
    
    public Page<Employee> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
