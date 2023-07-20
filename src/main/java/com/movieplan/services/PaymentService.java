package com.movieplan.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.Payment;
import com.movieplan.entity.User;
import com.movieplan.repository.PaymentRepository;
import com.movieplan.repository.TicketRepository;
import com.movieplan.repository.UserRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository prep;
	@Autowired
	private TicketRepository trep;
	@Autowired
	private UserRepository urep;

	private Logger logger  = LoggerFactory.getLogger(this.getClass());
	
	//CREATE METHODS
	public boolean addPayment(Payment p) {
		List<Integer> prices = p.getTickets().stream()
		.map(t -> t.getScreening())
		.map(s -> s.getMovie())
		.map(m -> m.getPrice())
		.collect(Collectors.toList());
		int sum = 0;
		for(Integer k : prices) {
			sum+=k;
		}
		if(p.getAmount() >= sum) {
			return prep.save(p)!=null;
		}
		else {
			logger.warn("The payment amount is less than the total of individual ticket prices!"
					+ " Please check and try again!");
			return false;
		}
	}
	
	//READ METHODS
	public Payment getPaymentById(int paymentId) {
		return prep.findById(paymentId).orElse(null);
	}
	
	public List<Payment> getPaymentByUser(String userId) {
		Optional<User> user = urep.findById(userId);
		if(user.isPresent()) {
			return prep.findByUser(user.get());
		}
		else {
			logger.error(" USER -> {}, is not present/ not registered!"
					+ " Please check the users table and try again!",userId);
			return null;
		}
	}
	
	//UPDATE METHODS
	
	public boolean updatePaymentUser(Payment p, String userId) {
		Optional<User> user = urep.findById(userId);
		if(user.isPresent()){
			Optional<Payment> payment = prep.findById(p.getId());
			if(payment.isPresent()) {
				payment.get().setUser(user.get());
				return prep.save(payment.get())!=null;
			}
			else {
				logger.warn("Payment with id -> {} is not present. Please check the payments table and try again!",p.getId());
				return false;
			}
		}
		else {
			logger.error("User {} is not present/ not registered! Please check the users table and try again!",userId);
			return false;
		}
	}
	
	public boolean updatePaymentAmount(Payment p, float amount) {
		Optional<Payment> payment = prep.findById(p.getId());
		if(payment.isPresent()) {
			List<Integer> prices = p.getTickets().stream()
					.map(t -> t.getScreening())
					.map(s -> s.getMovie())
					.map(m -> m.getPrice())
					.collect(Collectors.toList());
			int sum = 0;
			for(Integer k : prices) {
				sum+=k;
			}
			if(amount>=sum) {
				payment.get().setAmount(amount);
				return !Optional.of(prep.save(payment.get())).filter(pt -> pt.getId()==p.getId() && pt.getAmount() == amount).isEmpty();
			}
			else {
				logger.warn("The amount -> {} is less than the total sum of the individual ticket prices!"
						+ "Please check the amount and try again!",amount);
				return false;
			}
			
		}
		else {
			logger.error("Payment with id -> {}, is not present!"
					+ "Please check the payments table and try again!",p.getId());
			return false;
		}
	}
	
	//DELETE METHODS
	
	public boolean deletePayment(Payment p) {
		Optional<Payment> payment = prep.findById(p.getId());
		if(payment.isPresent()) {
			prep.deleteById(payment.get().getId());
			return prep.findById(p.getId()).isEmpty();
		}
		else {
			logger.error("Payment with id -> {} is not present. Please check the payments table and try again!",p.getId());
			return false;
		}
	}

}
