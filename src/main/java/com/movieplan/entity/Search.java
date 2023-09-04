package com.movieplan.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SearchTable")
public class Search {

	@Id
	@GeneratedValue
	private int id;
	
	private String userId;
	
	private String searchType;
	
	private String searchKeyword;
	
	@ManyToOne
	private FilterSet filterSet;
	
	private String sortField;
	
	private String sortOrder;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public FilterSet getFilterSet() {
		return this.filterSet;
	}

	public void setFilterSet(FilterSet filterSet) {
		this.filterSet = filterSet;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	
	
	
}
