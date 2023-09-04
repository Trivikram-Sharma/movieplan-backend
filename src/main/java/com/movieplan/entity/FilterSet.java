package com.movieplan.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "filterSets")
public class FilterSet {

	@Id
	@GeneratedValue
	private int id;
	
	private String title;
	
	private String language;
	
	private float amountMax;
	
	private String genre;
	
	private String showName;
	
	private LocalDate date;
	
	private String city;
	
	private String state;
	
	@OneToMany(mappedBy="filterSet")
	@JsonIgnore
	private List<Search> searches = new ArrayList<Search>();
	
	public FilterSet() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public float getAmountMax() {
		return amountMax;
	}

	public void setAmountMax(float amountMax) {
		this.amountMax = amountMax;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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

	public List<Search> getSearches() {
		return searches;
	}

	public void setSearches(List<Search> searches) {
		this.searches = searches;
	}
	
	
}
