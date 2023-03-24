package com.movieplan.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Movie")
public class Movie {
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "ticket_price", nullable = false)
	private int price;

	@Column(name = "language", nullable = false)
	private String language;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "release_date", nullable = false)
	private LocalDate releaseDate;

	@ColumnDefault(value = "enabled")
	private String status;

	@ManyToMany
	private List<Genre> genres;

	@OneToMany(mappedBy = "movie")
	private List<Screening> screenings;

	///////////////////////////////////////////////
	public Movie() {

	}

	public Movie(String id, String title, int price, String language, String description, LocalDate releaseDate) {
		this.id = id;
		this.title = title;
		this.price = price;
		this.language = language;
		this.description = description;
		this.releaseDate = releaseDate;
	}

	////////////////////////////////////////////////
	// Getters and setters
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void addGenre(Genre genre) {
		if (null != genre) {
			this.genres.add(genre);
		}
	}

	public void removeGenre(Genre genre) {
		this.genres.remove(genre);
	}
	
	public List<Genre> getGenres(){
		return this.genres;
	}

	public void addScreening(Screening screening) {
		if (null != screening) {
			this.screenings.add(screening);
		}
	}

	public void removeScreening(Screening screening) {
		if (this.screenings != null || !this.screenings.isEmpty()) {
			this.screenings.remove(screening);
		}
	}
	
	public List<Screening> getScreenings() {
		return this.screenings;
	}
	
	public LocalDate getReleaseDate() {
		return this.releaseDate;
	}
	
}
