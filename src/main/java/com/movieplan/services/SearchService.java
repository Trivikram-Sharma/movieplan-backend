package com.movieplan.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.Filter;
import com.movieplan.entity.Search;
import com.movieplan.repository.SearchRepository;

@Service
public class SearchService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SearchRepository srep;
	
	//CREATE METHODS
	public boolean addSearch(Search c) {
		Optional<Search> s = srep.findById(c.getId());
		if(s.isPresent()) {
			logger.error("Search with id->{} is already present! Please check the search table and try again!",c.getId());
			return false;
		}
		else {
			return !Optional.of(srep.save(c)).isEmpty();
		}
	}
	
	//READ METHODS
	public Search getSearchById(int id){
		Optional<Search> s = srep.findById(id);
		if(s.isEmpty()) {
			logger.error("No such search entity present with id -> {}.Please check search table and try again!",id);
			return null;
		}
		else {
			return s.get();
		}
	}
	
	public List<Search> getSearchesByUserId(String userId){
		return srep.findByUserId(userId);
		
	}
	
	public List<Search> getThreeFieldSearch(String userId, String searchType, String searchKeyword){
		return srep.findByUserIdAndSearchTypeAndSearchKeyword(userId, searchType, searchKeyword);
	}
	//UPDATE METHODS
	public boolean updateSearchFilters(Search s, List<Filter> f) {
		Optional<Search> sop = srep.findById(s.getId());
		if(sop.isPresent()) {
			s.setFilters(f);
			return !Optional.of(srep.save(s)).isEmpty();
		}
		else {
			logger.error("No search is present with id->{}. Please check the database and try again!",s.getId());
			return false;
		}
	}
}
