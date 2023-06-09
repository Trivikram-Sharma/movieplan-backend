package com.util;

import java.util.Map;

public class SearchRequest {
	private SearchType searchType;
	private String searchKeyword;
	private Map<FilterField,String> filters;
	private SortField sortField;
	
	//Getters and setters
	public SearchType getSearchType() {
		return searchType;
	}
	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public Map<FilterField, String> getFilters() {
		return filters;
	}
	public void setFilters(Map<FilterField, String> filters) {
		this.filters = filters;
	}
	public SortField getSortField() {
		return sortField;
	}
	public void setSortField(SortField sortField) {
		this.sortField = sortField;
	}
	
	
}
