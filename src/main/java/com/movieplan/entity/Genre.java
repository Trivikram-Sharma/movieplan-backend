package com.movieplan.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore
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
