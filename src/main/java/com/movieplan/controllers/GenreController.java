package com.movieplan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.Genre;
import com.movieplan.entity.Movie;
import com.movieplan.services.GenreService;

@RestController
@RequestMapping("/api/genre")
@CrossOrigin(origins = "http://localhost:4200")
public class GenreController {
	
	
	@Autowired
	private GenreService genreService;
	
	@PostMapping("/add")
	public boolean addGenre(@RequestBody Genre genre) {
		boolean result = genreService.addGenre(genre.getName());
		return result;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Genre>> getAllGenres(){
		List<Genre> results = genreService.getAllGenres();
		if(null == results || results.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<>(results, HttpStatus.OK);
		}
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Genre>> getAllGenresWithName(@RequestParam(required = true) String name){
		List<Genre> results = genreService.getAllGenresByName(name);
		if(null == results || results.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<>(results, HttpStatus.OK);
		}
	}
	
	@GetMapping("/search/movie")
	public List<Movie> getMoviesWithGenre(@RequestParam(required=true) String name){
		return genreService.getMoviesByGenre(name);
	}
	
	@DeleteMapping("/delete")
	public boolean deleteGenre(@RequestParam(required = true) String name) {
		return genreService.deleteGenre(name);
	}
}
