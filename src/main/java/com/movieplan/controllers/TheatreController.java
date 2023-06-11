package com.movieplan.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.Address;
import com.movieplan.entity.Theatre;
import com.movieplan.services.TheatreService;

@RestController
@RequestMapping("/api/theatre")
public class TheatreController {
	@Autowired
	private TheatreService thservice;

	//POST APIs
	@PostMapping("/add")
	public boolean addTheatre(@RequestBody Theatre theatre) {
		return thservice.addTheatre(theatre);
	}
	
	//GET APIs
	
	@GetMapping("/search/all")
	public List<Theatre> getAllTheatres(){
		return thservice.getAllTheatres();
	}
	
	@GetMapping("/search/id/{theatreId}")
	public Theatre getTheatreWithId(@PathVariable("theatreId") String theatreId) {
		return thservice.findTheatreById(Integer.parseInt(theatreId));
	}
	
	@GetMapping("/search/name/{theatreName}")
	public List<Theatre> getTheatreWithName(@PathVariable("theatreName") String theatreName) {
		return thservice.getTheatresWithName(theatreName);
	}
	@GetMapping("/search/screens/{theatreScreens}")
	public List<Theatre> getTheatreWithScreens(@PathVariable("theatreScreens") String theatreScreens) {
		return thservice.getTheatresWithScreens(Integer.parseInt(theatreScreens));
	}
	@GetMapping("/search/address/{theatreAddress}")
	public List<Theatre> getTheatreWithAddress(@PathVariable("theatreAddress") String theatreAddress) {
		return thservice.getAllTheatres().stream()
				.filter(t -> t.getAddress().toString().contains(theatreAddress)).collect(Collectors.toList());
	}
	
	//PATCH APIs
	
	@PatchMapping("/update/name/{theatreId}")
	public boolean updateTheatreName(@PathVariable("theatreId") String theatreId, @RequestParam(required = true) String name) {
		return thservice.updateTheatreName(name, thservice.findTheatreById(Integer.parseInt(theatreId)));
	}
	@PatchMapping("/update/screen/{theatreId}")
	public boolean updateTheatreScreens(@PathVariable("theatreId") String theatreId, @RequestParam(required = true) String screen) {
		return thservice.updateScreens(thservice.findTheatreById(Integer.parseInt(theatreId)), Integer.parseInt(screen));
	}
	@PatchMapping("/update/address/{theatreId}")
	public boolean updateTheatreAddress(@PathVariable("theatreId") String theatreId, @RequestBody Address address) {
		return thservice.updateTheatreAddress(thservice.findTheatreById(Integer.parseInt(theatreId)), address);
	}
	
	// DELETE APIs
	
	@DeleteMapping("/delete/screens/{theatreId}")
	public boolean deleteAllTheatreScreens(@PathVariable("theatreId") String theatreId) {
		return thservice.removeTheatreScreens(thservice.findTheatreById(Integer.parseInt(theatreId)));
	}
	
	@DeleteMapping("/delete/{theatreId}")
	public boolean deleteTheatre(@PathVariable("theatreId") String theatreId) {
		return thservice.deleteTheatre(thservice.findTheatreById(Integer.parseInt(theatreId)));
	}
	
}
