package com.movieplan.controllers;

import java.util.List;

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

import com.movieplan.entity.Ticket;
import com.movieplan.services.TicketService;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {
	@Autowired
	private TicketService tservice;
	
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
		return tservice.getTicketsByMovie(movieId);
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
	
	@PatchMapping("/update/movie/{id}")
	public boolean updateTicketMovie(@PathVariable("id") String id, @RequestParam(required = true) String movieId) {
		Ticket t = tservice.getTicketById(Integer.parseInt(id));
		return tservice.updateTicketMovie(t, movieId);
	}
	
	//DELETE APIs
	@DeleteMapping("/delete")
	public boolean deleteTicketById(@RequestParam(required = true) String ticketId) {
		Ticket t = tservice.getTicketById(Integer.parseInt(ticketId));
		return tservice.deleteTicket(t);
	}
	
	
}
