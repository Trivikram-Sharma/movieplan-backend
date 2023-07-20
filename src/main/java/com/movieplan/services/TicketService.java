package com.movieplan.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.Movie;
import com.movieplan.entity.Payment;
import com.movieplan.entity.Screening;
import com.movieplan.entity.Ticket;
import com.movieplan.entity.User;
import com.movieplan.repository.MovieRepository;
import com.movieplan.repository.PaymentRepository;
import com.movieplan.repository.ScreeningRepository;
import com.movieplan.repository.TicketRepository;
import com.movieplan.repository.UserRepository;

@Service
public class TicketService {

	@Autowired
	private TicketRepository trep;
	@Autowired
	private PaymentRepository prep;
	@Autowired
	private UserRepository urep;
	
	@Autowired
	private MovieRepository mrep;
	@Autowired
	private ScreeningRepository srep;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// CREATE METHODS

	public boolean addTicket(Ticket ticket) {
		return trep.save(ticket) != null;
	}

	// READ METHODS

	public Ticket getTicketById(int id) {
		return trep.findById(id).orElse(null);
	}

	public List<Ticket> getTicketsByUserId(String userId) {
		Optional<User> user = urep.findById(userId);
		if (user.isPresent()) {
			return trep.findByUser(user.get());
		} else {
			logger.error("User {} is not present/ not registered!", userId);
			return null;
		}
	}

	public List<Ticket> getTicketsByPaymentId(String paymentId) {
		Optional<Payment> payment = prep.findById(Integer.parseInt(paymentId));
		if (payment.isPresent()) {
			return trep.findByPayment(payment.get());
		} else {
			logger.error("payment Id -> {} is not present! Please check the payments table and try again! ", paymentId);
			return null;
		}
	}

	public List<Ticket> getTicketsByScreening(int screeningId) {
		Optional<Screening> screening = srep.findById(screeningId);
		if(screening.isPresent()) {
			return trep.findByScreening(screening.get());
		}
		else {
			logger.error("Screening with id -> {}, is not present. Please check screenings and try again!",screeningId);
			return null;
		}
	}
	// UPDATE METHODS
	public boolean updateTicketUser(Ticket ticket, String userId) {
		Optional<User> user = urep.findById(userId);
		if(user.isPresent()) {
			Optional<Ticket> t = trep.findById(ticket.getId());
			if(t.isPresent()) {
				t.get().setUser(user.get());
				return trep.save(t.get())!=null;
			}
			else {
				logger.warn("Ticket not present with ticket Id -> {}!"
						+ " Please check the tickets table and try agian!",ticket.getId());
				return false;
			}
		}
		else {
			logger.error("User {} is not present/ not registered!"
					+ " Please check the users table and try again! ",userId);
			return false;
		}
	}
	
	
//	public boolean updateTicketMovie(Ticket ticket, String movieId) {
//		Optional<Movie> movie = mrep.findById(movieId);
//		if(movie.isPresent()) {
//			Optional<Ticket> t = trep.findById(ticket.getId());
//			if(t.isPresent()) {
//				t.get().setMovie(movie.get());
//				return !Optional.of(trep.save(t.get()))
//						.filter(tk -> tk.getMovie().getId().equals(movieId)).isEmpty();
//			}
//			else {
//				logger.error("Ticket with id -> {} is not present! Please check tickets and try again!",ticket.getId());
//				return false;
//			}
//		}
//		else {
//			logger.error("Movie with id -> {}, is not present. Please check movies and try again!",movieId);
//			return false;
//		}
//	}
	public boolean updateTicketScreening(Ticket ticket, Screening screening) {
		Optional<Screening> s = srep.findById(screening.getId());
		//Placeholder to check that the screening is updated with the ticket
		boolean updatedScreening = false;
		if(s.isPresent()) {
			Optional<Ticket> t = trep.findById(ticket.getId());
			if(t.isPresent()) {
//				s.get().addTicket(t.get());
//				t.get().setScreening(s.get());
//				return !Optional.of(
//						srep.save(s.get())).isEmpty() &&
//						!Optional.of(trep.save(t.get())).filter(tk -> tk.getScreening().getId() == s.get().getId()).isEmpty();
				boolean notContainsTicket = Optional.of(
						s.get().getTickets().stream().filter(
								tkt -> tkt.getId() == t.get().getId()
								)
						).isEmpty();
				if(notContainsTicket) {
					s.get().addTicket(t.get());
					updatedScreening = !Optional.of(srep.save(s.get())).filter(scr -> scr.getTickets().contains(t.get())).isEmpty();
				}
				else {
					logger.warn("The ticket is already present in the screening! Thus, not updating the screening!");
					updatedScreening = true;
				}
				t.get().setScreening(s.get());
				return !Optional.of(trep.save(t.get())).filter(tk -> tk.getScreening().getId() == s.get().getId()).isEmpty() 
						&& updatedScreening;
			}
			else {
				logger.error("The ticket {} is not present! Please check the database and verify!",ticket);
				return false;
			}
		}
		else {
			logger.error("The Screening {} is not present! Please check the database and verify!",screening);
			return false;
		}
	}
	
	//DELETE METHODS
	public boolean deleteTicket(Ticket ticket) {
		Optional<Ticket> t = trep.findById(ticket.getId());
		if(t.isPresent()) {
			trep.deleteById(t.get().getId());
			return trep.findById(ticket.getId()).isEmpty();
		}
		else {
			logger.error("Ticket with id -> {} is not present. Please check the tickets table and verify!",ticket.getId());
			return false;
		}
	}

}
