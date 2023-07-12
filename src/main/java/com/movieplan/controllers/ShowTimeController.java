package com.movieplan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.ShowTimes;
import com.movieplan.services.ShowTimeService;

@RestController
@RequestMapping("/api/showtime")
@CrossOrigin(origins = "http://localhost:4200")
public class ShowTimeController {

	@Autowired
	private ShowTimeService stservice;

	// POST APIs
	/******** TEMPORARY ********/
	@PostMapping("/add")
	public boolean addShowTime(@RequestBody ShowTimes showTime) {
		return stservice.addShowTime(showTime.getShowName(), showTime.getStartTime().toString(),
				showTime.getEndTime().toString());
	}

	// GET APIs

	@GetMapping("/search/{showName}")
	public ShowTimes getShowTimesByName(@PathVariable("showName") String showName) {
		return stservice.getShowTimeByName(showName);
	}
	
	@GetMapping("/get/all")
	public List<ShowTimes> getAllShowTimes(){
		return stservice.getAllShowTimes();
	}

	// DELETE APIs
	@DeleteMapping("/delete/{showName}")
	public boolean deleteShowTime(@PathVariable("showName") String showName,
			@RequestParam(required = true) String starttime, @RequestParam(required = true) String endtime) {
		return stservice.deleteShowTime(showName, starttime, endtime);
	}
}
