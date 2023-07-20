package com.movieplan.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import com.movieplan.entity.Movie;
import com.movieplan.entity.Screening;
import com.movieplan.entity.Ticket;
import com.movieplan.services.MovieService;
import com.movieplan.services.ScreeningService;
import com.movieplan.services.TicketService;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {
	@Autowired
	private TicketService tservice;
	
	@Autowired
	private MovieService mservice;
	
	@Autowired
	private ScreeningService sservice;
	//POST MAPPINGS
	@PostMapping("/add")
	public boolean purchaseTicket(@RequestBody Ticket ticket) {
		return tservice.addTicket(ticket);
	}
	
	//GET MAPPINGS
	@GetMapping("/get/userId")
	public List<Ticket> getTicketsByUserId(@RequestParam(required = true) String userId){
		return tservice.getTicketsByUserId(userId);
	}
	@GetMapping("/get/movietitle")
	public List<Ticket> getTicketsByMovieId(@RequestParam(required = true) String movieId){
		Movie m = mservice.getMovieWithId(movieId).get();
		List<Screening> s = sservice.getScreeningsWithMovie(m);
		List<Ticket> result = new ArrayList<Ticket>();
		for(Screening sc: s) {
			result.addAll(tservice.getTicketsByScreening(sc.getId()));
		}
		result = result.stream().distinct().collect(Collectors.toList());
		return result;
	}
	@GetMapping("/get/id")
	public Ticket getTicketById(@RequestParam(required = true) String id) {
		return tservice.getTicketById(Integer.parseInt(id));
	}
	
	@GetMapping("/get/paymentid")
	public List<Ticket> getTicketsByPaymentId(@RequestParam(required = true) String paymentId){
		return tservice.getTicketsByPaymentId(paymentId);
	}
	
	//PATCH APIs
	@PatchMapping("/update/user/{id}")
	public boolean updateTicketUser(@PathVariable("id") String id, @RequestParam(required = true) String userId) {
		Ticket t = tservice.getTicketById(Integer.parseInt(id));
			return tservice.updateTicketUser(t, userId);
	}
	
	@PatchMapping("/update/screening/{id}")
	public boolean updateTicketScreening(@PathVariable("id") String id, @RequestParam(required = true) String screeningId) {
		Ticket t = tservice.getTicketById(Integer.parseInt(id));
		Screening s = sservice.getScreeningWithId(screeningId);
		return tservice.updateTicketScreening(t, s);
	}
	
	//DELETE APIs
	@DeleteMapping("/delete")
	public boolean deleteTicketById(@RequestParam(required = true) String ticketId) {
		Ticket t = tservice.getTicketById(Integer.parseInt(ticketId));
		return tservice.deleteTicket(t);
	}
	
	
}
