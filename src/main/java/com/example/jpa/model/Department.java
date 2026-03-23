package com.example.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dept")

public class Department {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	
	public Department() {}
	
	public Department(int id,String name) {
		this.id=id;
		this.name=name;
				
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	
	
}
