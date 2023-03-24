package com.movieplan.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Theatre {

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "theatre_name", nullable = false)
	private String name;

	@Column(name = "Number of Screens")
	private int screens;

	@OneToOne
	private Address address;

	@OneToMany
	private List<Screening> screenings;

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
		if (null != screening && screenings.size() < this.screens) {
			this.screenings.add(screening);
		}
	}

	public void removeScreening(Screening screening) {
		if (this.screenings != null || !this.screenings.isEmpty()) {
			this.screenings.remove(screening);
		}
	}

}
