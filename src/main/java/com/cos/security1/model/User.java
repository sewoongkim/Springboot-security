package com.cos.security1.model;

import java.time.LocalDateTime;

// import java.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	private String username;
	private String password;
	private String email;
	private String role; // Role_USER, Role_ADMIN

	private String provider;
	private String providerId;
	
	@CreatedDate
 	private LocalDateTime createDate;
	
	@LastModifiedDate
    private LocalDateTime modifyDate;

	@Builder
	public User(int id, String username, String password, String email, String role, String provider, String providerId,
			LocalDateTime createDate, LocalDateTime modifyDate) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
	}
	
	
	
}
