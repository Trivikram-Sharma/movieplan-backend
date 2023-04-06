package com.movieplan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieplan.entity.Payment;
import com.movieplan.entity.User;

public interface PaymentRepository extends JpaRepository<Payment,Integer>{

	List<Payment> findByUser(User user);
	
}
