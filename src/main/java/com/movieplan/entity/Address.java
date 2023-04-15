package com.movieplan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;

@Entity
public class Address {
	@Id
	private String building;
	
	private String street;
	
	private String area;
	
	@Column(nullable = false)
	private String city;
	
	@ColumnDefault(value = "'Andhra Pradesh'")
	private String state;
	
	@Column(nullable = false)
	@ColumnDefault(value = "'INDIA'")
	private String country;
	
	private String pincode;

	//Constructors
	public Address() {}
	
	public Address(String building, String street, String area, String city, String state, String country, String pincode) {
		this.building = building;
		this.street = street;
		this.area = area;
		this.city = city;
		this.state = state;
		this.country = country;
		this.pincode = pincode;
	}
	
	//Getters and setters
	public String getBuilding() {
		return building;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		//return "Address [building=" + building + ", street=" + street + ", area=" + area + ", city=" + city + ", state="
			//	+ state + ", country=" + country + ", pincode=" + pincode + "]";
		return building + ",\n" + street + ",\n" + area + ",\n" + city + ",\n" + state + ",\n" + country + ",\n" + pincode + ".";
	}
	
	
}
