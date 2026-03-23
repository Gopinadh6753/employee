package com.example.jpa.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.model.Department;
import com.example.jpa.service.DepartmentService;

@RestController
@RequestMapping("/Departments")
public class DepartmentController {

	private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return service.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Optional<Department> getDepartmentById(@PathVariable int id) {
        return service.getDeportmentById(id);
    }

    @PostMapping
    public Department createEmployee(@RequestBody Department dep) {
        return service.saveDepartment(dep);
    }

    @PutMapping("/{id}")
    public Department updateEmployee(@PathVariable int id, @RequestBody Department dep) {
        return service.updateDepartment(id, dep);
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable int id) {
        service.deleteDepartment(id);
        return "Employee deleted successfully!";
    }
}
