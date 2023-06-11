package com.movieplan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.Payment;
import com.movieplan.services.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	@Autowired
	private PaymentService pservice;

	
	//GET APIs
	@GetMapping("/get/id")
	public Payment getPaymentWithId(@RequestParam(required = true) String paymentId) {
		return pservice.getPaymentById(Integer.parseInt(paymentId));
	}
	
	@GetMapping("/get/userId")
	public List<Payment> getPaymentWithUserId(@RequestParam(required = true) String userId){
		return pservice.getPaymentByUser(userId);
	}
	
	//POST APIs
	
	//PUT API
	@PutMapping("/add")
	public boolean addPayment(@RequestBody Payment payment) {
		return pservice.addPayment(payment);
	}
	
	//PATCH APIs
	@PatchMapping("/update/user/{id}")
	public boolean updatePaymentUser(@PathVariable("id") String id,@RequestParam(required = true) String userId) {
		Payment p = pservice.getPaymentById(Integer.parseInt(id));
		if(null!=p) {
			return pservice.updatePaymentUser(p, userId);
		}
		else {
			return false;
		}
	}
	
	//DELETE APIs
	@DeleteMapping("/delete")
	public boolean deletePayment(@RequestParam(required = true) String paymentId) {
		Payment p = pservice.getPaymentById(Integer.parseInt(paymentId));
		if(null!=p) {
			return pservice.deletePayment(p);
		}
		else {
			return false;
		}
	}
}
