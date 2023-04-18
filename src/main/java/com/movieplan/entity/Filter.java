package com.movieplan.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Filters")
public class Filter {

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToMany
	private List<Search> searches;
	
	private String filterField;
	
	private String filterValues;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Search> getSearches() {
		return searches;
	}

	public void setSearches(List<Search> searches) {
		this.searches = searches;
	}

	public String getFilterField() {
		return filterField;
	}

	public void setFilterField(String filterField) {
		this.filterField = filterField;
	}

	public String getFilterValues() {
		return filterValues;
	}

	public void setFilterValues(String filterValues) {
		this.filterValues = filterValues;
	}
	
	
	
	
}
