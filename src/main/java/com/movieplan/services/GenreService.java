package com.movieplan.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.Genre;
import com.movieplan.entity.Movie;
import com.movieplan.repository.GenreRepository;

@Service
public class GenreService {

	@Autowired
	private GenreRepository genreRepo;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	// Find Genre by Name
	public Genre findGenreByName(String name) {
		List<Genre> genres = genreRepo.findByName(name);
		if (genres.size() == 1) {
			return genres.get(0);
		} else {
			logger.warn("Multiple genres are present with name {}. Please check the database and try again!", name);
			return null;
		}
	}

	//Get Movies with a genre
	public List<Movie> getMoviesByGenre(String name) {
		return findGenreByName(name).getMovies();
	}
	
	//Get Movies with multiple genres
	public Set<Movie> getMoviesByGenres(List<Genre> genres) {
		Set<Movie> aggregateResult = new HashSet<>();
		for(Genre genre: genres) {
			List<Movie> movies = genre.getMovies();
			for(Movie movie:movies) {
				if(!aggregateResult.contains(movie)) {
					aggregateResult.add(movie);
				}
			}
		}
		return aggregateResult;
	}

	//Get all genres
	public List<Genre> getAllGenres(){
		return genreRepo.findAll();
	}
	
	//get all genres by name
	public List<Genre> getAllGenresByName(String name){
		return genreRepo.findByName(name);
	}
	
	//Deleting a Genre
	public boolean deleteGenre(String genreName) {
		List<Genre> genres = genreRepo.findByName(genreName);
		if (genres.size() == 1) {
			int genreId = genres.get(0).getId();
			//Remove genre from all associated movies
			List<Movie> relevantMovies = getMoviesByGenre(genreName);
			
			for(Movie movie : relevantMovies) {
				movie.removeGenre(genres.get(0));
				genres.get(0).removeMovie(movie);
			}
			if (genres.get(0).getMovies().isEmpty()) {
				genreRepo.deleteById(genreId);
			}
			else {
				logger.warn("All associated movies are not deleted from the genre {}. Please check the database and try again!", genreName);
				return false;
			}
			if (genreRepo.findById(genreId).isPresent()) {
				logger.error("Genre deletion with name {}, is unsuccessful! Please check the database and try again!",
						genreName);
				return false;
			} else {
				logger.info("Genre deletion with name {}, is Successful!", genreName);
				return true;
			}
		} else {
			logger.warn("Multiple genres are present with name {}. Please check the database and try again!",
					genreName);
			return false;
		}

	}
	//Adding a genre
	public boolean addGenre(String name) {
		Genre genre = new Genre(name);
		genreRepo.save(genre);

		List<Genre> genres = genreRepo.findByName(name);
		if (genres.size() == 0) {
			logger.error("The new genre {} is not added successfully! Please check the database and try again!", name);
			return false;
		}
		else if (genres.size() > 0) {
			logger.info("The new Genre with name {} is added successfully! There are total {} genres with the name {}",name,genres.size(), name);
			return true;
		}
		else {
			logger.error("Unknown error."
					+ "The number of elements with genre name {} is {}, which is Invalid."
					+ " Please check the database and try again!", name, genres.size());
			return false;
		}

	}
}
