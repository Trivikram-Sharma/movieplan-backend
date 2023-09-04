package com.movieplan.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne
	private User user;
	
	@OneToMany(mappedBy = "payment")
	@JsonIgnore
	private List<Ticket> tickets = new ArrayList<Ticket>();
	
	
	private float amount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void addTicket(Ticket ticket) {
		if(null!=ticket) {
			this.tickets.add(ticket);
		}
	}
	public void removeTicket(Ticket ticket) {
		if(this.tickets.contains(ticket)) {
			this.tickets.remove(ticket);
		}
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
	
}
