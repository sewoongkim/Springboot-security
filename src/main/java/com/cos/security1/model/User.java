package com.cos.security1.model;

import java.time.LocalDateTime;

// import java.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User {
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	private String username;
	private String password;
	private String email;
	private String role; // Role_USER, Role_ADMIN
	private LocalDateTime createDate;
}
