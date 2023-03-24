package com.movieplan.entity;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "show_times")
public class ShowTimes {

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "show_name", nullable = false)
	private String showName;

	@Column(name = "start_time")
	private LocalTime startTime;

	@Column(name = "end_time")
	private LocalTime endTime;

	@OneToMany(mappedBy = "showTimes")
	private List<Screening> screenings;

	// Constructors
	public ShowTimes() {
	}

	public ShowTimes(String showName, LocalTime startTime, LocalTime endTime) {
		this.showName = showName;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	//////////////////////////////////

	// Getters and setters
	public int getId() {
		return id;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public List<Screening> getScreenings() {
		return this.screenings;
	}

	public void addScreening(Screening screening) {
		if (screening != null) {
			this.screenings.add(screening);
		}
	}
	
	public void removeScreening(Screening screening) {
		if(this.screenings != null || !this.screenings.isEmpty()) {
		this.screenings.remove(screening);
		}
	}
}
