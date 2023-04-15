package com.movieplan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;

	@ColumnDefault(value = "'inactive'")
	private String status;
	
	//Constructors
	public User() {}

	// Getters and setters
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
