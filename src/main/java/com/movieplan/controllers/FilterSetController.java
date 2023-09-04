package com.movieplan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.FilterSet;
import com.movieplan.services.FilterSetService;

@RestController
@RequestMapping("/api/filterset")
@CrossOrigin(origins = "http://localhost:4200")
public class FilterSetController {
	
	@Autowired
	FilterSetService fsservice;

	@GetMapping("/get/all")
	public List<FilterSet> getAllFilterSets(){
		return fsservice.getAllFilterSets();
	}
}
