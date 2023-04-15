package com.movieplan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "admin")
public class Admin {

	@Id
	@Column(name = "admin_username")
	private String adminUserName;

	@Column(name = "admin_password")
	private String adminPassword;
	
	@Column(name = "login_status")
	@ColumnDefault(value = "'inactive'")
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
