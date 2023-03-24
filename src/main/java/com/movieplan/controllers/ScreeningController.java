package com.movieplan.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

import com.movieplan.entity.Movie;
import com.movieplan.entity.Screening;
import com.movieplan.entity.ShowTimes;
import com.movieplan.entity.Theatre;
import com.movieplan.services.MovieService;
import com.movieplan.services.ScreeningService;
import com.movieplan.services.ShowTimeService;
import com.movieplan.services.TheatreService;

@RestController
@RequestMapping("/api/screening")
public class ScreeningController {

	
	@Autowired
	private ScreeningService sservice;
	
	@Autowired
	private TheatreService tservice;
	
	@Autowired
	private MovieService mservice;
	
	@Autowired
	private ShowTimeService stservice;
	//POST APIs
	
	@PostMapping("/add")
	public boolean addScreening(@RequestBody Screening screening) {
		return sservice.addScreening(screening);
	}
	
	//GET APIs
	
	@GetMapping("/search/all")
	public List<Screening> getAllScreenings(){
		return sservice.getAllScreenings();
	}
	@GetMapping("/search/theatre")
	public List<Screening> getAllScreeningsWithTheatre(@RequestParam(required = true) String theatreId){
		Theatre theatre = tservice.findTheatreById(Integer.parseInt(theatreId));
		if(null != theatre) {
			return sservice.getScreeningsInTheatre(theatre);
		}
		else {return null;}
	}
	@GetMapping("/search/movie")
	public List<Screening> getAllScreeningsWithMovie(@RequestParam(required = true) String movieId){
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if(movie.isPresent()) {
			return sservice.getScreeningsWithMovie(movie.get());
		}
		else {
			return null;
		}
	}
	@GetMapping("/search/showtime")
	public List<Screening> getAllScreeningsAtShowTime(@RequestParam(required = true) String showTime){
		ShowTimes st = stservice.getShowTimeByName(showTime);
		if(null != st) {
			return sservice.getScreeningsWithShowTime(st);
		}
		else {return null;}
	}
	@GetMapping("/search/date")
	public List<Screening> getAllScreeningsOnDate(@RequestParam(required = true) String date){
		LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
		return sservice.getScreeningsOnDate(date1);
	}
	
	//PATCH APIs
	
	@PatchMapping("{screeningId}/update/theatre")
	public boolean updateScreeningTheatre(@PathVariable("screeningId") String screeningId, @RequestParam(required = true) String theatreId) {
		Theatre theatre = tservice.findTheatreById(Integer.parseInt(theatreId));
		Screening s = sservice.getScreeningWithId(screeningId);
		if(null!=theatre && null!= s) {
			return sservice.updateScreeningTheatre(s, theatre);
		}
		else {
			return false;
		}
	}
	@PatchMapping("{screeningId}/update/movie")
	public boolean updateScreeningMovie(@PathVariable("screeningId") String screeningId, @RequestParam(required = true) String movieId) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		Screening s = sservice.getScreeningWithId(screeningId);
		if(movie.isPresent() && null!=s ) {
			return sservice.updateScreeningMovie(s, movie.get());
		}
		else {return false;}
	}
	@PatchMapping("{screeningId}/update/showtime")
	public boolean updateScreeningShowTime(@PathVariable("screeningId") String screeningId, @RequestParam(required = true) String showName) {
		ShowTimes st = stservice.getShowTimeByName(showName);
		Screening s = sservice.getScreeningWithId(screeningId);
		if(null!=st && null!=s) {
			return sservice.updateScreeningShowTime(s, st);
		}
		else {return false;}
	}
	@PatchMapping("{screeningId}/update/status")
	public boolean updateScreeningStatus(@PathVariable("screeningId") String screeningId, @RequestParam(required = true) String status) {
		Screening s = sservice.getScreeningWithId(screeningId);
		if(null!=s) {
			return sservice.updateScreeningStatus(s, status);
		}
		else {
			return false;
		}
	}
	@PatchMapping("/{screeningId}/update/date")
	public boolean updateScreeningDate(@PathVariable("screeningId") String screeningId, @RequestParam(required = true) String date1) {
		Screening s = sservice.getScreeningWithId(screeningId);
		if(null!=s) {
			return sservice.updateScreeningDate(s, LocalDate.parse(date1, DateTimeFormatter.ISO_LOCAL_DATE));
		}
		else {return false;}
	}
	
	// DELETE APIs
	
	@DeleteMapping("/{screeningId}/delete/theatre")
	public boolean deleteScreeningTheatre(@PathVariable("screeningId") String screeningId) {
		Screening s = sservice.getScreeningWithId(screeningId);
		if(null!=s) {
			return sservice.deleteScreeningTheatre(s);
		}
		else {
			return false;
		}
	}
	@DeleteMapping("/{screeningId}/delete/movie")
	public boolean deleteScreeningMovie(@PathVariable("screeningId") String screeningId) {
		Screening s = sservice.getScreeningWithId(screeningId);
		if(null!=s) {
			return sservice.deleteScreeningMovie(s);
		}
		else {
			return false;
		}
	}
	@DeleteMapping("/{screeningId}/delete/showtime")
	public boolean deleteScreeningShowTime(@PathVariable("screeningId") String screeningId) {
		Screening s = sservice.getScreeningWithId(screeningId);
		if(null!=s) {
			return sservice.deleteScreeningShowTime(s);
		}
		else {
			return false;
		}
	}
	@DeleteMapping("/{screeningId}/delete/date")
	public boolean deleteScreeningDate(@PathVariable("screeningId") String screeningId) {
		Screening s = sservice.getScreeningWithId(screeningId);
		if(null!=s) {
			return sservice.deleteScreeningDate(s);
		}
		else {
			return false;
		}
	}
	@DeleteMapping("/delete")
	public boolean deleteScreening(@RequestParam(required = true) String screeningId) {
		Screening s = sservice.getScreeningWithId(screeningId);
		if(null!=s) {
			return sservice.deleteScreening(s);
		}
		else {
			return false;
		}
		
	}
}
