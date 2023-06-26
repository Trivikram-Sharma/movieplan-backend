package com.movieplan.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.Genre;
import com.movieplan.entity.Movie;
import com.movieplan.services.GenreService;
import com.movieplan.services.MovieService;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

	@Autowired
	private MovieService mservice;

	@Autowired
	private GenreService genreService;

	@PostMapping("/add")
	public boolean addMovie(@RequestBody Movie movie) {
		String id = movie.getId();
		String tilte = movie.getTitle();
		int price = movie.getPrice();
		String language = movie.getLanguage();
		String description = movie.getDescription();
		LocalDate releaseDate = movie.getReleaseDate();
		String status = movie.getStatus();
		String genre = movie.getGenres().get(0).getName();
		return mservice.addMovie(id, tilte, price, language, description, releaseDate, status, genre)
				&& mservice.updateMovieGenres(movie.getGenres().subList(1, movie.getGenres().size() - 1), movie);
	}

	//GET APIs
	
	@GetMapping("/getNewId")
	public String getNewId(@RequestParam(required = true) String title,
			@RequestParam(required = true) String description,
			@RequestParam(required = true) String language,
			@RequestParam(required = true) String releasedate) {
		Movie m = new Movie();
		m.setTitle(title);
		m.setLanguage(language);
		m.setDescription(description);
		m.setReleaseDate(LocalDate.parse(releasedate, DateTimeFormatter.ISO_LOCAL_DATE));
		return mservice.getNewIdFromPresentMovies(
				mservice.getParticularMovies(title, language, description, releasedate), m);
	}
	
	@GetMapping("/id")
	public Movie getMovieWithId(@RequestParam(required = true) String id) {
		Optional<Movie> movie = mservice.getMovieWithId(id);
		if(movie.isPresent()) {
			return movie.get();
		}
		else {
			return null;
		}
	}
	
	@GetMapping("/search")
	public List<Movie> getAllMovies() {
		return mservice.getAllMovies();
	}

	@GetMapping("/search/title")
	public List<Movie> getMoviesByTitle(@RequestParam(required = true) String title) {
		return mservice.getMovieByName(title);
	}

	@GetMapping("/search/pricerange")
	public List<Movie> getMoviesInPriceRange(@RequestParam(required = true) String lprice,
			@RequestParam(required = true) String hprice) {
		return mservice.getMovieByPriceRange(Integer.parseInt(lprice), Integer.parseInt(hprice));
	}

	@GetMapping("/search/language")
	public List<Movie> getMoviesOfLanguage(@RequestParam(required = true) String language) {
		return mservice.getMoviesByLangauage(language);
	}

	@GetMapping("/search/genre")
	public Set<Movie> getMoviesByGenre(@RequestParam(required = true) String genre) {
		return mservice.getMoviesByGenre(genre);
	}

	@GetMapping("/search/releaseDate")
	public List<Movie> getMoviesByReleaseDate(@RequestParam(required = true) String releaseDate) {
		return mservice.getMoviesByReleaseDate(LocalDate.parse(releaseDate, DateTimeFormatter.ISO_LOCAL_DATE));
	}

	@GetMapping("/search/title/{title}")
	public List<Movie> getMoviesContainingTitle(@PathVariable("title") String title) {
		return mservice.getMoviesContainingName(title);
	}

	@GetMapping("/search/price")
	public List<Movie> getMoviesWithTicketPrice(@RequestParam(required = true) String price) {
		return mservice.getMoviesWithPriceTicket(Integer.parseInt(price));
	}

	@GetMapping("/search/enabled")
	public List<Movie> getEnabledMovies() {
		return mservice.getEnabledMovies();
	}

	@GetMapping("/search/disabled")
	public List<Movie> getDisabledMovies() {
		return mservice.getDisabledMovies();
	}

//UPDATE APIs	

	@PatchMapping("/update/title/{movieId}")
	public boolean updateMovieTitle(@PathVariable("movieId") String movieId,
			@RequestParam(required = true) String title) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			movie.get().setTitle(title);
			return mservice.updateMovieTitle(movie.get());
		} else {
			return false;
		}
	}

	@PatchMapping("/update/price/{movieId}")
	public boolean updateMoviePrice(@PathVariable("movieId") String movieId,
			@RequestParam(required = true) String price) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			movie.get().setPrice(Integer.parseInt(price));
			return mservice.updateTicketPrice(movie.get());
		} else {
			return false;
		}
	}

	@PatchMapping("/update/language/{movieId}")
	public boolean updateMovieLanguage(@PathVariable("movieId") String movieId,
			@RequestParam(required = true) String language) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			movie.get().setLanguage(language);
			return mservice.updateMovieLanguage(movie.get());
		} else {
			return false;
		}
	}

	@PatchMapping("/update/description/{movieId}")
	public boolean updateMovieDescription(@PathVariable("movieId") String movieId,
			@RequestParam(required = true) String description) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			movie.get().setDescription(description);
			return mservice.updateDescription(movie.get());
		} else {
			return false;
		}

	}

	@PatchMapping("/update/enable/{movieId}")
	public boolean enableMovie(@PathVariable("movieId") String movieId) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			return mservice.enableMovie(movie.get());
		} else {
			return false;
		}

	}

	@PatchMapping("/update/disable/{movieId}")
	public boolean disableMovie(@PathVariable("movieId") String movieId) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			return mservice.disableMovie(movie.get());
		} else {
			return false;
		}

	}
	
	@PatchMapping("/update/releaseDate/{movieId}")
	public boolean updateMovieReleaseDate(@PathVariable("movieId") String movieId,
			@RequestParam(required = true) String releaseDate) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if(movie.isPresent()) {
			return mservice.updateReleaseDate(movie.get());			
		}
		else {
			return false;
		}
	}

	@PatchMapping("/update/addGenre/{movieId}")
	public boolean updateMovieWithGenre(@PathVariable("movieId") String movieId,
			@RequestParam(required = true) String genre) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			return mservice.updateMovieGenre(genreService.findGenreByName(genre), movie.get());
		} else {
			return false;
		}

	}

	@PostMapping("/update/addGenres/{movieId}")
	public boolean updateMovieWithGenres(@PathVariable("movieId") String movieId,
			@RequestBody(required = true) List<String> genres) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			List<Genre> genresList = genres.stream().map(genre -> genreService.findGenreByName(genre))
					.collect(Collectors.toList());
			return mservice.updateMovieGenres(genresList, movie.get());
		} else {
			return false;
		}

	}

	// DELETE APIs

	@DeleteMapping("/delete/genre/{movieId}")
	public boolean deleteGenreFromMovie(@PathVariable("movieId") String movieId,
			@RequestParam(required = true) String genre) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			return mservice.deleteGenreFromMovie(genreService.findGenreByName(genre), movie.get());
		} else {
			return false;
		}
	}

	@DeleteMapping("/delete/genres/{movieId}")
	public boolean deleteGenresFromMovie(@PathVariable("movieId") String movieId, @RequestBody List<String> genres) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if (movie.isPresent()) {
			List<Genre> genreList = genres.stream().map(genre -> genreService.findGenreByName(genre))
					.collect(Collectors.toList());
			long booleanCount = genreList.stream()
					.map(genreElem -> mservice.deleteGenreFromMovie(genreElem, movie.get())).filter(b -> b == false)
					.count();
			return booleanCount == 0;
		} else {
			return false;
		}

	}
	
	@DeleteMapping("/delete/{movieId}")
	public boolean deleteMovie(@PathVariable("movieId") String movieId) {
		Optional<Movie> movie = mservice.getMovieWithId(movieId);
		if(movie.isPresent()) {
			return mservice.deleteMovie(movie.get());
		}
		else {
			return false;
		}
	}

}
