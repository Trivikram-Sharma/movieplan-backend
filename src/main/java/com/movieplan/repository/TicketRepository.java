package com.movieplan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieplan.entity.Movie;
import com.movieplan.entity.Payment;
import com.movieplan.entity.Ticket;
import com.movieplan.entity.User;

public interface TicketRepository extends JpaRepository<Ticket, Integer>{

	List<Ticket> findByUser(User user);
	
	List<Ticket> findByPayment(Payment payment);
	
	List<Ticket> findByMovie(Movie movie);
	
}
