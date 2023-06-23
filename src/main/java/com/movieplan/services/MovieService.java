package com.movieplan.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.Genre;
import com.movieplan.entity.Movie;
import com.movieplan.entity.Screening;
import com.movieplan.repository.GenreRepository;
import com.movieplan.repository.MovieRepository;
import com.movieplan.repository.ScreeningRepository;
import com.movieplan.repository.ShowTimesRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepo;

	@Autowired
	private ShowTimesRepository showTimeRepo;

	@Autowired
	private GenreRepository genreRepo;

	@Autowired
	private ScreeningRepository screeningRepo;

	@Autowired
	private GenreService genreService;

	@Autowired
	private ShowTimeService showTimeService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/************** CREATE METHODS *****************/

	// Add a Movie
	public boolean addMovie(String id, String title, int price, String language, String description,
			LocalDate releaseDate, String status, String genre) {
		Movie movie = new Movie(id, title, price, language, description, releaseDate);
		movie.setStatus(null == status ? "enabled" : status);
		List<Genre> genreList = genreRepo.findByName(genre);

		if (genreList.size() == 1) {
			movie.addGenre(genreList.get(0));
			List<Movie> presentMovies = movieRepo.getParticularMovie(title, language, description, releaseDate);
			String newId = getNewIdFromPresentMovies(presentMovies, movie);
			if (null != newId && newId.equals(id)) {
				movieRepo.save(movie);
				Optional<Movie> savedMovie = movieRepo.findById(id);
				if (savedMovie.isPresent()) {
					return true;
				} else {
					logger.error(
							"Below Movie is not saved successfully! Please check the movie table of database, and try again!\n{}",
							movie);
					return false;
				}
			} else if (null == newId) {
				logger.error("Please check the movie database for the id -> {}, and try again.", id);
				return false;
			} else {
				logger.warn("Invalid Id provided! Please check if any movies present with id {} and try again.", id);
				return false;
			}

		} else {
			logger.warn("Multiple genres found with genre name -> {}. Please check the database and verify", genre);
			return false;
		}

	}

	// Helper method to create new id for a movie to be added, in case multiple
	// movies are present with same title, language, description, release date
	public String getNewIdFromPresentMovies(List<Movie> presentMovies, Movie currentMovie) {
		if (null == presentMovies || presentMovies.isEmpty()) {
			return currentMovie.getTitle().replace(" ", "_") + "_" + currentMovie.getLanguage() + "_"
					+ currentMovie.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
		} else if (!presentMovies.isEmpty() && presentMovies.size() == 1) {
			return presentMovies.get(0).getId() + "_1";
		} else if (presentMovies.size() > 1) {

			List<String> presentIds = presentMovies.stream().map(movie -> movie.getId()).collect(Collectors.toList());
			boolean checkNoDuplicate = presentIds.size() == presentIds.stream().distinct().collect(Collectors.toList())
					.size();
			if (checkNoDuplicate) {
				int totalIds = 0;
				for (int i = 0; i < presentIds.size(); i++) {
					if (i > 0) {
						int currentIdLength = presentIds.get(i).length();
						String lastChar = String.valueOf(presentIds.get(i).charAt(currentIdLength - 1));
						if (lastChar.equals(String.valueOf(i))) {
							totalIds++;
						}
					}
				}
				return currentMovie.getTitle().replace(" ", "_") + "_" + currentMovie.getLanguage() + "_"
						+ currentMovie.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + "_"
						+ String.valueOf(totalIds + 1);
			} else {
				logger.error(
						"Movie Table of the database might have duplicate entries! Please check the database and verify!");
				return null;
			}
		} else {
			logger.warn("Unknown error. Please check if the movies are present with the id {} and try again!",
					currentMovie.getId());
			return null;
		}
	}

	/************** READ METHODS *****************/
	
	//Get Particular Movies List with same tile, language, description, release Date
	public List<Movie> getParticularMovies(String title, String language, String description, String releaseDate){
		return movieRepo.getParticularMovie(title, language, description, 
				LocalDate.parse(releaseDate, DateTimeFormatter.ISO_LOCAL_DATE));
	}
	
	//Get Movie By Id
	public Optional<Movie> getMovieWithId(String id) {
		return movieRepo.findById(id);
	}

	//Get All Movies
	public List<Movie> getAllMovies() {
		return movieRepo.findAll();
	}
	
	// Get Movies By Title
	public List<Movie> getMovieByName(String title) {
		return movieRepo.findByTitle(title);
	}

	// Get Movies By Language
	public List<Movie> getMoviesByLangauage(String language) {
		return movieRepo.findByLanguage(language);
	}

	// Get Movies in Ticket Price Range
	public List<Movie> getMovieByPriceRange(int lprice, int hprice) {
		return movieRepo.getMoviesInPriceRange(hprice, lprice);
	}
	//Get movies of ticket price
	public List<Movie> getMoviesWithPriceTicket(int price) {
		return movieRepo.findByPrice(price);
	}

	// Get Movies by Genre
	public Set<Movie> getMoviesByGenre(String genre) {
		List<Genre> genreList = genreRepo.findByName(genre);
		return genreService.getMoviesByGenres(genreList);
	}

	// Get Movies containing name in title
	public List<Movie> getMoviesContainingName(String name) {
		return movieRepo.getMoviesContainingName(name);
	}

	// Get Enabled Movies
	public List<Movie> getEnabledMovies() {
		return movieRepo.findByStatus("enabled");
	}

	// Get Disabled Movies
	public List<Movie> getDisabledMovies() {
		return movieRepo.findByStatus("disabled");
	}

	// Get Movies by release date
	public List<Movie> getMoviesByReleaseDate(LocalDate date) {
		return movieRepo.findByReleaseDate(date);
	}

	/************** UPDATE METHODS *****************/

	// Update Movie Title
	public boolean updateMovieTitle(Movie movie) {
		List<Movie> moviesWithTitle = getMovieByName(movie.getTitle());
		if (moviesWithTitle.size() == 1) {
			movieRepo.save(movie);
			if (movieRepo.findById(movie.getId()).isPresent()) {
				logger.info("Movie title updated to {} Successfully.", movie.getTitle());
				return true;
			} else {
				logger.error(
						"Movie title update not successful, on movie [id -> {}].\n Please check the database and try again!",
						movie.getId());
				return false;
			}
		} else {
			logger.warn(
					"Multiple movies are present with title {}. Checking movies on the basis of the given id -> {}.",
					movie.getTitle(), movie.getId());
			int count = movieRepo.findAllById(movie.getId()).size();
			if (count == 1) {
				movieRepo.save(movie);
				if (movieRepo.findById(movie.getId()).isPresent()
						&& movieRepo.findById(movie.getId()).get().getTitle().equals(movie.getTitle())) {
					logger.info("Movie title updated to {} Successfully.", movie.getTitle());
					return true;
				} else {
					logger.error(
							"Movie title not updated successfully on checking with Id. Please check the database for movies with id-> {} and verify!",
							movie.getId());
					return false;
				}
			} else {
				logger.warn("Multiple movies found with id -> {}. Please check database and verify.", movie.getId());
				return false;
			}
		}
	}

	// Update Movie Ticket Price
	public boolean updateTicketPrice(Movie movie) {
		Optional<Movie> existingObject = movieRepo.findById(movie.getId());
		if (existingObject.isPresent()) {
			movieRepo.save(movie);
			if (existingObject.isPresent() && existingObject.get().getPrice() == movie.getPrice()) {
				logger.info("Movie price updated to {} Successfully.", movie.getTitle());
				return true;
			} else {
				logger.error("Movie ticket price not updated successfully! Result retrieved by id -> {},"
						+ " is as shown below \n {}", movie.getId(), existingObject.get());
				return false;
			}
		} else {
			logger.error("Movie with id {} is not present. Please check the database and verify", movie.getId());
			return false;
		}

	}

	// Update Movie Language
	public boolean updateMovieLanguage(Movie movie) {
		Optional<Movie> existingObject = movieRepo.findById(movie.getId());
		if (existingObject.isPresent()) {
			movieRepo.save(movie);

			if (existingObject.isPresent() && existingObject.get().getLanguage().equals(movie.getLanguage())) {
				logger.info(
						"Movie Language updated successfully. The old and new objects are as shown below. \n updated object -> {}",
						existingObject.get());
				return true;
			} else {
				logger.error("Movie Language " + "not updated successfully! Result retrieved by id -> {},"
						+ " is as shown below \n {}", movie.getId(), existingObject.get());
				return false;

			}
		} else {
			logger.error("The below movie is not present. Please check the database and verify.\n {}", movie);
			return false;
		}
	}

	// Update the Movie Description
	public boolean updateMovieDescription(Movie movie) {
		Optional<Movie> existingObject = movieRepo.findById(movie.getId());
		if (existingObject.isPresent()) {
			movieRepo.save(movie);
			if (existingObject.isPresent() && existingObject.get().getDescription().equals(movie.getDescription())) {
				logger.info(
						"The movie description updated successfully. The new object is as shown below.\n old object -> {} \n New Object -> {}",
						existingObject.get());
				return true;
			} else {
				logger.error("Movie Description not updated successfully! Result retrieved by id -> {},"
						+ " is as shown below \n {}", movie.getId(), existingObject.get());
				return false;
			}
		} else {
			logger.error("The below movie is not present. Please check the database and verify.\n {}", movie);
			return false;
		}
	}

	// Enable a Movie
	public boolean enableMovie(Movie movie) {
		Optional<Movie> existingMovie = movieRepo.findById(movie.getId());
		if (existingMovie.isPresent()) {
			movie.setStatus("enabled");
			movieRepo.save(movie);
			if (existingMovie.isPresent() && existingMovie.get().getStatus().equals("enabled")) {
				logger.info("Movie {} is enabled successfully.", movie.getTitle());
				return true;
			} else {

				logger.error(
						"Movie {} is not enabled successfully! please check the database if a movie with id-> {} is present",
						movie.getTitle(), movie.getId());
				return false;
			}
		} else {
			logger.error("The below movie is not present. Please check the database and verify.\n {}", movie);
			return false;

		}
	}

	// Disable a Movie
	public boolean disableMovie(Movie movie) {
		Optional<Movie> existingMovie = movieRepo.findById(movie.getId());
		if (existingMovie.isPresent()) {
			movie.setStatus("disabled");
			movieRepo.save(movie);
			if (existingMovie.isPresent() && existingMovie.get().getStatus().equals("disabled")) {
				logger.info("Movie {} is disabled successfully.", movie.getTitle());
				return true;
			} else {

				logger.error(
						"Movie {} is not disabled successfully! please check the database if a movie with id-> {} is present",
						movie.getTitle(), movie.getId());
				return false;
			}
		} else {
			logger.error("The below movie is not present. Please check the database and verify.\n {}", movie);
			return false;

		}
	}

	// Update release date
	public boolean updateReleaseDate(Movie movie) {
		Optional<Movie> existingMovie = movieRepo.findById(movie.getId());
		if (existingMovie.isPresent()) {
			movieRepo.save(movie);
			if (existingMovie.isPresent()
					&& existingMovie.get().getReleaseDate().toString().equals(movie.getReleaseDate().toString())) {
				logger.info("Movie Release date updated successfully. Please find updated movie below \n {}",
						existingMovie.get());
				return true;
			} else {

				logger.error(
						"Movie release date is not updated successfully! please check the database if a movie with id-> {} is present",
						movie.getId());
				return false;
			}

		} else {
			logger.error("The below movie is not present. Please check the database and verify.\n {}", movie);
			return false;

		}
	}

	// Update description
	public boolean updateDescription(Movie movie) {
		Optional<Movie> existingMovie = movieRepo.findById(movie.getId());
		if (existingMovie.isPresent()) {
			movieRepo.save(movie);
			if (existingMovie.isPresent() && existingMovie.get().getDescription().equals(movie.getDescription())) {
				logger.info("Movie Description updated successfully. Please find updated movie below \n {}",
						existingMovie.get());
				return true;
			} else {

				logger.error(
						"Movie Description is not updated successfully! please check the database if a movie with id-> {} is present",
						movie.getId());
				return false;
			}

		} else {
			logger.error("The below movie is not present. Please check the database and verify.\n {}", movie);
			return false;

		}
	}

	// Update movie genres multiple at a time
	public boolean updateMovieGenres(List<Genre> genres, Movie movie) {
		Optional<Movie> presentMovie = movieRepo.findById(movie.getId());
		if (presentMovie.isPresent()) {
			List<Genre> existingGenres = presentMovie.get().getGenres();
			Optional<Genre> existingGenre;
			for (Genre genre : genres) {
				if (!existingGenres.contains(genre)) {
					existingGenre = genreRepo.findById(genre.getId());
					if (existingGenre.isPresent()) {
						movie.addGenre(genre);
						existingGenre.get().addMovie(movie);
						genreRepo.save(existingGenre.get());
					} else {
						logger.error(
								"The genre {} is not present in the database. Please check the genre table and verify!",
								genre.getName());
						return false;
					}
				} else {
					genres.remove(genre);
				}
			}
			movieRepo.save(movie);
			if (presentMovie.isPresent() && presentMovie.get().getGenres().size() == genres.size()
					&& presentMovie.get().getGenres().containsAll(genres)) {
				logger.info("Movie genres updated successfully! The updated movie is as shown below \n {}",
						presentMovie.get());
				return true;
			} else {
				logger.error(
						"Movie genre update not successful! Please check the database if a movie with id->{} is present",
						movie.getId());
				return false;
			}
		} else {
			logger.error("No movie is present with id->{}. Please check the database and verify!", movie.getId());
			return false;
		}
	}

	// Update movie genre - Single genre
	public boolean updateMovieGenre(Genre genre, Movie movie) {
		Optional<Movie> presentMovie = movieRepo.findById(movie.getId());
		if (presentMovie.isPresent()) {
			if (presentMovie.get().getGenres().contains(genre)) {
				logger.warn("The genre {} is already added to the movie {}! Please check the database and try again!",
						genre.getName(), presentMovie.get().getTitle());
				return false;
			} else {
				if (genreRepo.findById(genre.getId()).isPresent()) {
					movie.addGenre(genre);
					movieRepo.save(movie);

					if (presentMovie.isPresent() && presentMovie.get().getGenres().contains(genre)) {
						logger.info("Movie {} successfully updated with genre {}.", presentMovie.get().getTitle(),
								genre.getName());
						return true;
					} else {
						logger.error(
								"Movie not updated with the give genre {}! Please check the database and verify the below movie object\n {}",
								genre.getName(), presentMovie.get());
						return false;
					}
				} else {
					logger.error(
							"The genre {} is not present in the database. Please check the genre table and verify!",
							genre.getName());
					return false;
				}
			}
		} else {
			logger.error("No movie is present with id->{}. Please check the database and verify!", movie.getId());
			return false;

		}
	}

	// Update Screenings - Multiple Screenings
	public boolean updateMovieScreenings(Movie movie, List<Screening> screenings) {
		Optional<Movie> existingMovie = movieRepo.findById(movie.getId());
		if (existingMovie.isPresent()) {
			List<Screening> existingScreenings = existingMovie.get().getScreenings();
			Optional<Screening> scheduledScreening;
			for (Screening s : screenings) {
				if (!existingScreenings.contains(s)) {
					scheduledScreening = screeningRepo.findById(s.getId());
					if (scheduledScreening.isPresent()) {
						existingMovie.get().addScreening(s);
						scheduledScreening.get().setMovie(movie);
						screeningRepo.save(scheduledScreening.get());
					} else {
						logger.error(
								"Screening with id -> {} is not present. Please check the screenings table and verify!",
								s.getId());
						return false;
					}
				} else {
					screenings.remove(s);
				}
			}

			movieRepo.save(movie);
			if (existingMovie.isPresent() && existingMovie.get().getScreenings().size() == screenings.size()
					&& existingMovie.get().getScreenings().containsAll(screenings)) {
				logger.info("Movie Screenings updated successfully! The updated movie is as shown below \n {}",
						existingMovie.get());
				return true;
			} else {
				logger.error(
						"Movie screenings update not successful! Please check the database if a movie with id->{} is present",
						movie.getId());
				return false;
			}

		} else {
			logger.error("The movie with id-> {} is not present. Please check the database and verify.", movie.getId());
			return false;
		}
	}

	// Update Movie Screening - Single Screening
	public boolean updateMovieScreening(Movie movie, Screening screening) {
		Optional<Movie> existingMovie = movieRepo.findById(movie.getId());
		Optional<Screening> presentScreening = screeningRepo.findById(screening.getId());

		if (existingMovie.isPresent()) {
			if (presentScreening.isPresent()) {
				existingMovie.get().addScreening(presentScreening.get());
				presentScreening.get().setMovie(existingMovie.get());
				movieRepo.save(existingMovie.get());
				screeningRepo.save(presentScreening.get());
				if (presentScreening.isPresent() && presentScreening.get().getMovie() == existingMovie.get()
						&& movieRepo.findById(movie.getId()).isPresent()
						&& existingMovie.get().getScreenings().contains(presentScreening.get())) {
					logger.info(
							"Movie screening updated successfully. Please find the updated movie and screening as shown below. \n {} \n {}",
							existingMovie.get(), presentScreening.get());
					return true;
				} else {
					logger.error(
							"Movie / screening update error. Pleae check the following movie and screening and verify!\n{} \n {}",
							existingMovie.get(), presentScreening.get());
					return false;
				}
			} else {
				logger.error(
						"The screening with id -> {} is not present. please check the screenings in database and verify!",
						screening.getId());
				return false;
			}
		} else {
			logger.error("The movie with id -> {} is not present. Please check the movie database and verify!",
					movie.getId());
			return false;
		}
	}

	/***************** DELETE METHODS ******************/

	// Delete Genre From Movie
	public boolean deleteGenreFromMovie(Genre genre, Movie movie) {
		Optional<Movie> presentMovie = movieRepo.findById(movie.getId());
		Optional<Genre> presentGenre = genreRepo.findById(genre.getId());

		if (presentMovie.isPresent()) {
			if (presentGenre.isPresent()) {
				if (presentMovie.get().getGenres().contains(presentGenre.get())) {
					presentMovie.get().removeGenre(presentGenre.get());
					presentGenre.get().removeMovie(presentMovie.get());
					movieRepo.save(presentMovie.get());
					genreRepo.save(presentGenre.get());

					if (presentMovie.isPresent() && presentGenre.isPresent()
							&& !presentMovie.get().getGenres().contains(presentGenre.get())) {
						logger.info("Genre {} successfully removed from movie {}", presentGenre.get().getName(),
								presentMovie.get().getTitle());
						return true;
					} else {
						logger.error(
								"The Genre {} delete fom movie {} failed. Please check the following movie and genre in the database and try again! \n {} \n {}",
								movie, genre);
						return false;
					}

				} else {
					logger.error(
							"The genre {} is not associated with movie {}. Thus genre delete from the movie failed.",
							genre.getName(), movie.getTitle());
					return false;
				}
			} else {
				logger.error("The gener {} is not present in the database. Please check the genre table and verify!",
						genre.getName());
				return false;
			}
		} else {
			logger.error("The movie {} is not present in the database, thus delete failed.", movie.getTitle());
			return false;
		}
	}

	// Delete Screening from a Movie
	public boolean deleteMovieScreening(Movie movie, Screening screening) {
		Optional<Movie> existingMovie = movieRepo.findById(movie.getId());
		Optional<Screening> presentScreening = screeningRepo.findById(screening.getId());

		if (existingMovie.isPresent()) {
			if (presentScreening.isPresent()) {
				if (existingMovie.get().getScreenings().contains(presentScreening.get())) {
					existingMovie.get().removeScreening(presentScreening.get());
					presentScreening.get().setMovie(null);
					movieRepo.save(existingMovie.get());
					screeningRepo.save(presentScreening.get());
					if (!existingMovie.get().getScreenings().contains(presentScreening.get())) {
						logger.info("Screening with id-> {} successfully removed from movie {}", screening.getId(),
								movie.getTitle());
						return true;
					} else {
						logger.error(
								"Screening deletion form the movie failed. Please check the below movie and screening in the database and try again!\n {} \n {}",
								existingMovie.get(), presentScreening.get());
						return false;
					}

				} else {
					logger.warn(
							"The screening with id -> {}, is not associated with the movie {}. Thus, delete failed.",
							screening.getId(), movie.getTitle());
					return false;
				}
			} else {
				logger.error("The screening with id-> {}  not present. Please check the database and verify!",
						screening.getId());
				return false;
			}
		} else {
			logger.error("The movie {} not present in the database, thus delete of screening failed", movie.getTitle());
			return false;
		}
	}

	public boolean deleteMovie(Movie movie) {
		Optional<Movie> presentMovie = movieRepo.findById(movie.getId());
		if (presentMovie.isPresent()) {
			List<Genre> genres = presentMovie.get().getGenres();
			List<Screening> screenings = presentMovie.get().getScreenings();
			boolean genresRemoved = false, screeningsRemoved = false;
			for (Genre genre : genres) {
				genresRemoved = deleteGenreFromMovie(genre, movie);
				if (!genresRemoved) {
					genresRemoved = false;
					break;
				}
			}
			for (Screening screening : screenings) {
				screeningsRemoved = deleteMovieScreening(movie, screening);
				if (!screeningsRemoved) {
					screeningsRemoved = false;
					break;
				}
			}

			if (genresRemoved && screeningsRemoved) {
				movieRepo.delete(presentMovie.get());
				if (movieRepo.findById(movie.getId()).isPresent()) {
					logger.error("Movie deletion not successful! Please check the below movie in database and try again \n {}", presentMovie.get());
					return false;
				} else {
					logger.info("Movie {} deleted successfully!",movie.getTitle());
					return true;
				}
			} else {
				logger.error(
						"Genres/ Screenings not deleted from the movie {}. Thus delete of movie failed. Please check the below movie in database and try again!\n {}",
						presentMovie.get());
				return false;
			}
		} else {
			logger.error("The movie {} is not present in the database. Please check movie table and verify",
					movie.getId());
			return false;
		}
	}

}
