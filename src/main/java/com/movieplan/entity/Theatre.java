package com.movieplan.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Theatre {

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "theatre_name", nullable = false)
	private String name;

	@Column(name = "Number_of_Screens")
	private int screens;

	@OneToOne
	private Address address;

	@OneToMany
	@JsonIgnore
	private List<Screening> screenings = new ArrayList<Screening>();

	// Getters and Setters

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public int getScreens() {
		return this.screens;
	}

	public void setScreens(int screens) {
		this.screens = screens;
	}

	public List<Screening> getScreenings() {
		return this.screenings;
	}

	public void addScreening(Screening screening) {
		if (null != screening && null!=this.screenings && this.screenings.size() < this.screens) {
			this.screenings.add(screening);
		}
	}

	public void removeScreening(Screening screening) {
		if (this.screenings != null || !this.screenings.isEmpty()) {
			this.screenings.remove(screening);
		}
	}

}
