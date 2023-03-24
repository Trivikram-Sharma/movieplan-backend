package com.movieplan.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin {

	@Id
	@Column(name = "admin_username")
	private String adminUserName;

	@Column(name = "admin_password")
	private String adminPassword;
	
	@Column(name = "login status")
	@ColumnDefault(value = "inactive")
	private String status;

	// Getters and setters
	public String getAdminUserName() {
		return adminUserName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
