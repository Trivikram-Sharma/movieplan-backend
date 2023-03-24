package com.movieplan.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Genre")
public class Genre {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	@Column(name = "genre_name", nullable = false)
	private String name;

	@ManyToMany(mappedBy = "genres")
	private List<Movie> movies;
	
	//Constructors
	public Genre() {
		
	}
	public Genre(String name) {
		super();
		this.name = name;
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addMovie(Movie movie) {
		if (null != movie) {
			this.movies.add(movie);
		}
	}
	
	public void removeMovie(Movie movie) {
		this.movies.remove(movie);
	}
	
	public List<Movie> getMovies() {
		return this.movies;
	}
}
