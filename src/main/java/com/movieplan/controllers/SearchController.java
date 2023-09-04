package com.movieplan.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.Search;
import com.movieplan.services.FilterSetService;
import com.movieplan.services.SearchService;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:4200")
public class SearchController {
	
	@Autowired
	SearchService sservice;
	
	@Autowired
	FilterSetService fsservice;
	
	@PostMapping("/add")
	public boolean addSearch(@RequestBody Search search) {
		return sservice.addSearch(search);
	}
	
	@GetMapping("/get/all")
	public List<Search> getAllSearches(){
		return sservice.getAllSearches();
	}
	
	@GetMapping("/get/userId")
	public List<Search> getAllSearchesByUserId(@RequestParam(required = true) String userId){
		List<Search> s = sservice.getSearchesByUserId(userId);
		return s;
	}
	
	@PostMapping("/getParticular")
	public List<Search> getParticularSearch(@RequestBody Search search){
		return sservice.getParticularSearch(search.getUserId(),search.getSearchType(),
				search.getSearchKeyword(),
				search.getSortField(),search.getSortOrder(), search.getFilterSet());
	}
	
	@PatchMapping("/update/{id}")
	public boolean updateSearch(@RequestBody Search search,@PathVariable("id") String id) {
		return sservice.updateSearch(search);
	}
	
	@DeleteMapping("/delete")
	public boolean deleteSearch(@RequestParam(required = true) String searchId) {
		Search searchOp = sservice.getSearchById(Integer.parseInt(searchId));
		if(searchOp==null) {
			return false;
		}
		else {
			return sservice.deleteSearch(searchOp);
		}
	}
}
