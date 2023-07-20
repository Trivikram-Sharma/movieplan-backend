package com.movieplan.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Screening {
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	private Theatre theatre;
	
	@ManyToOne
	private Movie movie;
	
	@ManyToOne
	private ShowTimes showTime;
	
	private LocalDate date;
	
	@OneToMany(mappedBy = "screening")
	@JsonIgnore
	private List<Ticket> tickets = new ArrayList<Ticket>();
	
	@ColumnDefault(value = "'Not started'")
	private String status;

	//Constructors
	
	public Screening() {}
	
	
	/************/
	//Getters and setters
	public Theatre getTheatre() {
		return theatre;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public ShowTimes getShowTime() {
		return showTime;
	}

	public void setShowTime(ShowTimes showTime) {
		this.showTime = showTime;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return this.status;
	}
	public void addTicket(Ticket t) {
		if(null!=t) {
			this.tickets.add(t);			
		}
	}
	public List<Ticket> getTickets(){
		return this.tickets;
	}
	public void removeTicket(Ticket t) {
		if(this.tickets.contains(t)) {
			this.tickets.remove(t);
		}
	}
	
	
}
