package com.movieplan.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.Movie;
import com.movieplan.entity.Screening;
import com.movieplan.entity.ShowTimes;
import com.movieplan.entity.Theatre;
import com.movieplan.repository.MovieRepository;
import com.movieplan.repository.ScreeningRepository;
import com.movieplan.repository.ShowTimesRepository;
import com.movieplan.repository.TheatreRepository;

@Service
public class ScreeningService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ScreeningRepository srep;

	@Autowired
	private TheatreRepository trep;

	@Autowired
	private MovieRepository mrep;

	@Autowired
	private ShowTimesRepository showTimerep;

	@Autowired
	private TheatreService tservice;

	@Autowired
	private MovieService mservice;

	@Autowired
	private ShowTimeService stservice;

	/******************* CREATE METHODS *****************/
	// Add a screening
	public boolean addScreening(Screening screening) {
		Optional<Screening> scr = srep.findById(screening.getId());
		if (scr.isEmpty()) {
			boolean theatrePreCheck = Optional.of(screening.getTheatre())
					.filter(t -> (t.getScreenings()==null) ||  (t.getScreenings().size() < t.getScreens() && !t.getScreenings().contains(screening)))
					.isPresent();
			boolean moviePreCheck = Optional.of(screening.getMovie())
					.filter(m -> (m.getScreenings()==null || !m.getScreenings().contains(screening)) && m.getStatus().equals("enabled")).isPresent();
			boolean showTimePreCheck = Optional.of(screening.getShowTime())
					.filter(st -> st.getScreenings()==null || !st.getScreenings().contains(screening)).isPresent();
			//Initializing Date pre check
			boolean datePreCheck = false;
			//If the theatre has no screenings at all, determine if date is valid, i.e., not a past date.
			if(screening.getTheatre().getScreenings()==null) {
				datePreCheck = !screening.getDate().isBefore(LocalDate.now());
			}
			else {
				//If theatre does have screenings, checking to ensure that the screenings in that theatre, on that day, on the particular showtime, are less
				//than total screens available in the theatre
			datePreCheck = screening.getTheatre().getScreenings().stream()
					.filter(s -> s.getDate().equals(screening.getDate()))
					.filter(s -> s.getShowTime().getShowName().equals(screening.getShowTime().getShowName()))
					.count() < screening.getTheatre().getScreens() && !screening.getDate().isBefore(LocalDate.now());
			}
			//Separated Conditions for simplicity and readability
			boolean isSameDate = screening.getDate().equals(LocalDate.now());
			boolean exactTime = screening.getShowTime().getStartTime().equals(LocalTime.now());
			boolean at15Minutes = LocalTime.now().equals(screening.getShowTime().getStartTime().plusMinutes(15L));
			boolean betweenTime = LocalTime.now().isBefore(screening.getShowTime().getStartTime().plusMinutes(15L))
					&& LocalTime.now().isAfter(screening.getShowTime().getStartTime());
			boolean isPastDate = screening.getDate().isBefore(LocalDate.now());
			boolean isFutureDate = screening.getDate().isAfter(LocalDate.now());
			if (isSameDate && (exactTime || at15Minutes || betweenTime)) {
				screening.setStatus("Started");
			} else if (isSameDate && LocalTime.now().isAfter(screening.getShowTime().getStartTime().plusMinutes(15L))
					&& LocalTime.now().isBefore(screening.getShowTime().getEndTime())) {
				screening.setStatus("Running");
			} else if (isPastDate || (isSameDate &&LocalTime.now().isAfter(screening.getShowTime().getEndTime()))) {
				screening.setStatus("Closed");
			} else if ( (isFutureDate) ||(isSameDate && LocalTime.now().isBefore(screening.getShowTime().getStartTime()))) {
				screening.setStatus("Not Started");
			} else {
				logger.error(
						"Unknown error on show Time. Please check the below screening's time and try again! \n Screening -> {}, \n Time -> {}",
						screening, LocalTime.now());
				return false;
			}
			boolean statusPreCheck = (!isPastDate && (isSameDate && !screening.getShowTime().getStartTime().isBefore(LocalTime.now())))
					&& !(screening.getStatus().equals("Running") || screening.getStatus().equals("Closed"));
			if (theatrePreCheck && moviePreCheck && showTimePreCheck && datePreCheck && statusPreCheck) {
				screening.getTheatre().addScreening(screening);
				screening.getMovie().addScreening(screening);
				screening.getShowTime().addScreening(screening);
//				trep.save(screening.getTheatre());
//				mrep.save(screening.getMovie());
				showTimerep.save(screening.getShowTime());
				tservice.updateTheatreScreening(screening.getTheatre(), screening);
				mservice.updateMovieScreening(screening.getMovie(), screening);
				return Optional.of(srep.save(screening)).isPresent();
			} else {
				logger.error(
						"One or more of the following conditions is not satisfied. Thus, screening is not added to the database! \n "
								+ "theatrePreCheck -> {}, \n moviePreCheck -> {}, \n showTimePreCheck -> {}, \n datePreCheck -> {}, \n statusPreCheck-> {}",
								theatrePreCheck, moviePreCheck, showTimePreCheck, datePreCheck, statusPreCheck);
				return false;
			}
		} else {
			logger.error("The screening with id -> {} is already present. Please check the database and verify!",
					screening.getId());
			return false;
		}
	}

	/*************** READ METHODS ******************/
	//Get Screening with Id
	public Screening getScreeningWithId(String id) {
		Optional<Screening> s = srep.findById(Integer.parseInt(id));
		if(s.isPresent()) {
			return s.get();
		}
		else {
			return null;
		}
	}
	
	//Get All Screenings
	public List<Screening> getAllScreenings() {
		return srep.findAll();
	}
	
	// Get All Screenings In a theatre
	public List<Screening> getScreeningsInTheatre(Theatre theatre) {
		return srep.findByTheatre(theatre);
	}

	// Get All Screenings of a Movie
	public List<Screening> getScreeningsWithMovie(Movie movie) {
		return srep.findByMovie(movie);
	}

	// Get All Screenings of a showTime
	public List<Screening> getScreeningsWithShowTime(ShowTimes showTime) {
		return srep.findByShowTime(showTime);
	}

	// Get all screenings on a date
	public List<Screening> getScreeningsOnDate(LocalDate date) {
		return srep.findByDate(date);
	}

	/****************** UPDATE METHODS ****************/

	// Update screening theatre
	public boolean updateScreeningTheatre(Screening screening, Theatre theatre) {
		Optional<Screening> scr = srep.findById(screening.getId());
		Optional<Theatre> thr = trep.findById(theatre.getId());
		if (scr.isPresent()) {
			if (thr.isPresent()) {
				long occupiedScreens = thr.get().getScreenings().stream()
						.filter(s -> s.getStatus().equals("Running") || s.getStatus().equals("Started")).count();
				if (thr.get().getScreens() > occupiedScreens) {
					boolean oldTheatreUpdated = tservice.removeTheatreScreening(scr.get().getTheatre(), scr.get());
					boolean newTheatreUpdated = tservice.updateTheatreScreening(thr.get(), scr.get());
					if (oldTheatreUpdated && newTheatreUpdated) {
						scr.get().setTheatre(thr.get());
						return Optional.of(srep.save(scr.get())).filter(s -> s.getTheatre().equals(thr.get()))
								.isPresent();
					} else {
						logger.error(
								"Something went wrong when updating the old and new theatres."
										+ " Please check the below theatres and verify.\n {} \n {}",
								scr.get().getTheatre(), thr.get());
						return false;
					}
				} else {
					logger.error("Theatre is fully occupied! Please check the theatres and verify \n {}", thr.get());
					return false;
				}

			} else {
				logger.error("The below theatre is not present in the database! Please check and verify! \n {}",
						theatre);
				return false;
			}
		} else {
			logger.error("The below screening is not present in the database! Please check and verify! \n {}",
					screening);
			return false;
		}
	}

	// Update Screening Movie
	public boolean updateScreeningMovie(Screening screening, Movie movie) {
		Optional<Screening> scr = srep.findById(screening.getId());
		Optional<Movie> m = mrep.findById(movie.getId());
		if (scr.isPresent()) {
			if (m.isPresent()) {
				boolean moviePreCheck = m.get().getStatus().equals("enabled")
						&& scr.get().getStatus().equals("Not Started");
				if (moviePreCheck) {
					boolean oldMovieUpdated = mservice.deleteMovieScreening(scr.get().getMovie(), scr.get());
					boolean newMovieUpdated = mservice.updateMovieScreening(m.get(), scr.get());
					if (oldMovieUpdated && newMovieUpdated) {
						scr.get().setMovie(m.get());
						return Optional.of(srep.save(scr.get()))
								.filter(s -> s.getMovie().getId().equals(m.get().getId())).isPresent();
					} else {
						logger.error(
								"Something went wrong with old and new movies to be updated on screening. please check the below two movies and verify."
										+ "\n Old Movie -> {} \n New Movie -> {}",
								scr.get().getMovie(), m.get());
						return false;
					}
				} else {
					logger.error(
							"The Movie {} is disabled! Thus, the movie cannot be screened. Check the below movie and verify\n {} ",
							m.get().getTitle(), m.get());
					return false;
				}
			} else {
				logger.error("The below theatre is not present in the database! Please check and verify! \n {}", movie);
				return false;
			}
		} else {
			logger.error("The below screening is not present in the database! Please check and verify! \n {}",
					screening);
			return false;
		}
	}

	// Update Screening Status
	public boolean updateScreeningStatus(Screening screening, String status) {
		Optional<Screening> scr = srep.findById(screening.getId());
		if (scr.isPresent()) {
			scr.get().setStatus(status);
			return Optional.of(srep.save(scr.get())).filter(s -> s.getStatus().equals(status)).isPresent();
		} else {
			logger.error("The below screening is not present in the database! Please check and verify \n {}",
					screening);
			return false;
		}
	}

	// Update Screening ShowTime
	public boolean updateScreeningShowTime(Screening screening, ShowTimes showTime) {
		Optional<Screening> scr = srep.findById(screening.getId());
		Optional<ShowTimes> stime = showTimerep.findById(showTime.getId());

		if (scr.isPresent()) {
			if (stime.isPresent()) {
				long showTimeConflictScreenings = stime.get().getScreenings().stream()
						.filter(s -> s.getId() == scr.get().getId()
								|| (s.getTheatre().getId() == scr.get().getTheatre().getId())
										&& s.getMovie().getId().equals(scr.get().getMovie().getId())
										&& s.getStatus().equals(scr.get().getStatus())
										&& s.getDate().isEqual(scr.get().getDate()))
						.count();
				boolean isPastDate = scr.get().getDate().isBefore(LocalDate.now());
				boolean isToday = scr.get().getDate().isEqual(LocalDate.now());
				boolean isFutureDate = scr.get().getDate().isAfter(LocalDate.now());
				//Initializing showtimecheck to false. i.e., checking if the current time is before the start of the show. However, this
				//applies only if the date of screening is today. On the other hand, for past date screenings, this will be left false and for future dates,
				//it will be true as the show hasn't even started. Thus, the three if conditions below.
				boolean showtimecheck = false;
				if(isPastDate) {
					//For past dates, it doesn't apply as there is no point in changing showtime for a past screening.
					showtimecheck = false;
				}
				else if(isToday) {
					//For today, the current time must be before the start time of the show. or conversely, the show start time, should be after the current time.
					showtimecheck = stime.get().getStartTime().isAfter(LocalTime.now());
				}
				else if(isFutureDate) {
					//For future dates, this is directly true, as the show hasn't even started.
					showtimecheck = true;
				}
				boolean showTimePreCheck = showtimecheck
						&& showTimeConflictScreenings == 0 && (scr.get().getStatus().equals("Not Started"));
				if (showTimePreCheck) {
					scr.get().getShowTime().removeScreening(scr.get());
					stime.get().addScreening(scr.get());
					boolean oldShowTimeUpdated = Optional.of(showTimerep.save(scr.get().getShowTime()))
							.filter(st -> st.getScreenings().contains(scr.get())).isEmpty();
					boolean newShowTimeUpdated = Optional.of(showTimerep.save(stime.get()))
							.filter(st -> !st.getScreenings().contains(scr.get())).isEmpty();
					if (oldShowTimeUpdated && newShowTimeUpdated) {
						scr.get().setShowTime(stime.get());
						return Optional.of(srep.save(scr.get()))
								.filter(s -> s.getShowTime().getShowName().equals(stime.get().getShowName()))
								.isPresent();
					} else {
						logger.error(
								"Something went wrong with the old and new show times to be updated. Please check and verify the below show times. \n"
										+ "Old Show Time -> {} \n New Show Time -> {}",
								scr.get().getShowTime(), stime.get());
						return false;
					}
				}else if(scr.get().getShowTime().getId() == stime.get().getId()) {
					logger.warn("The new desired showtime, is already the same as the existing showtime of the screening! Thus, not carrying out further operations..");
					return true;
				}
				else {
					logger.error(
							"ShowTime Pre check has failed! Please check if there are conflicting screenings during the target showtime or"
									+ " if the showTime is after the current time." + " \n Target ShowTime -> {} ",
							stime.get());
					logger.warn("Also, please check the status of the screening -> {}", scr.get().getStatus());
					return false;
				}
			} else {
				logger.error("The below ShowTime is not present in the database! Please check and verify! \n {}",
						showTime);
				return false;
			}
		} else {
			logger.error("The below screening is not present in the database! Please check and verify! \n {}",
					screening);
			return false;
		}
	}

	// Update date of the screening
	public boolean updateScreeningDate(Screening screening, LocalDate date) {
		Optional<Screening> scr = srep.findById(screening.getId());
		List<Screening> screenings = getScreeningsOnDate(date);
		if (scr.isPresent()) {
			long conflictingScreeningsOnDate = screenings.stream().filter(
					s -> s.getId() == scr.get().getId() || (s.getTheatre().getId() == scr.get().getTheatre().getId()
							&& s.getMovie().getId().equals(scr.get().getMovie().getId())
							&& s.getShowTime().getShowName().equals(scr.get().getShowTime().getShowName())
							&& s.getStatus().equals(scr.get().getStatus())))
					.count();
			boolean datePreCheck = !date.isBefore(LocalDate.now()) && scr.get().getStatus().equals("Not Started")
					&& conflictingScreeningsOnDate == 0;
			if (datePreCheck) {
				scr.get().setDate(date);
				return Optional.of(srep.save(scr.get())).filter(s -> s.getDate().isEqual(date)).isPresent();
			}else if(scr.get().getDate().equals(date)) {
				logger.warn("The new desired date is same as the existing date of the screening! Thus, not updating/carrying out any operations!");
				return true;
			} 
			else {
				logger.error("The date is not valid! Please provide a date after today and verify!"
						+ " Also check the status of the screening.\n" + "Screening -> {}, \n Target Date -> {}",
						scr.get(), date);
				return false;
			}
		} else {
			logger.error("The below screening is not present in the database! Please check and verify! \n {}",

					screening);
			return false;
		}
	}

	/***************** DELETE METHODS ****************/
	// Delete Theatre from screening
	public boolean deleteScreeningTheatre(Screening screening) {
		Optional<Screening> s = srep.findById(screening.getId());
		if (s.isPresent()) {
			boolean theatrePreCheck = !(s.get().getStatus().equals("Started") || s.get().getStatus().equals("Running"));
			if (theatrePreCheck) {
				boolean theatreScreeningRemoved = tservice.removeTheatreScreening(s.get().getTheatre(), s.get());
				if (theatreScreeningRemoved) {
					s.get().setTheatre(null);
					return Optional.of(srep.save(s.get())).filter(sr -> null != sr.getTheatre()).isEmpty();
				} else {
					logger.warn(
							"{} \n Failed to remove below screening from above theatre. Please check and verify. \n {}",
							s.get().getTheatre(), s.get());
					return false;
				}
			} else {
				logger.error("The screening is in '{}' state. Thus, it cannot be removed from the theatre.",
						s.get().getStatus());
				return false;
			}
		} else {
			logger.error("The screening is not present in the database, Please check and verify!\n {}", screening);
			return false;
		}
	}

	// Delete Movie from screening
	public boolean deleteScreeningMovie(Screening screening) {
		Optional<Screening> s = srep.findById(screening.getId());
		if (s.isPresent()) {
			boolean moviePreCheck = !(s.get().getStatus().equals("Started") || s.get().getStatus().equals("Running"));
			if (moviePreCheck) {
				boolean movieScreeningRemoved = mservice.deleteMovieScreening(s.get().getMovie(), s.get());
				if (movieScreeningRemoved) {
					s.get().setMovie(null);
					return Optional.of(srep.save(s.get())).filter(sr -> null != sr.getMovie()).isEmpty();
				} else {
					logger.warn(
							"{} \n Failed to remove above screening from below movie. Please check and verify! \n {}",
							s.get(), s.get().getMovie());
					return false;
				}
			} else {
				logger.error("The screening is in '{}' state. Thus, it cannot be removed from the theatre.",
						s.get().getStatus());
				return false;
			}
		} else {
			logger.error("The screening is not present in the database, Please check and verify!\n {}", screening);
			return false;
		}
	}

	// Delete ShowTime from Screening
	public boolean deleteScreeningShowTime(Screening screening) {
		Optional<Screening> s = srep.findById(screening.getId());
		if(s.isPresent()) {
				boolean showTimePreCheck = !(s.get().getStatus().equals("Started") || s.get().getStatus().equals("Running"));
				if(showTimePreCheck) {
					s.get().getShowTime().removeScreening(s.get());
					boolean showTimeScreeningRemoved = Optional.of(showTimerep.save(s.get().getShowTime())).filter( st -> st.getScreenings().contains(s.get())).isEmpty();
					if(showTimeScreeningRemoved) {
						s.get().setShowTime(null);
						return Optional.of(srep.save(s.get())).filter(sr -> null != sr.getShowTime()).isEmpty();
					}
					else {
						logger.warn("{} \n Removal of above screening from below showtime has failed! Please check and verify! \n {}",s.get(), s.get().getShowTime());
						return false;
					}
				}
				else {
					logger.error("The screening is in '{}' state. Thus, it cannot be removed from the theatre.",
							s.get().getStatus());
					return false;					
				}
		}
		else {			
			logger.error("The screening is not present in the database, Please check and verify!\n {}", screening);
			return false;
		}
	}
	
	//Delete screening date
	public boolean deleteScreeningDate(Screening screening) {
		Optional<Screening> s = srep.findById(screening.getId());
		if(s.isPresent()) {
			boolean datePreCheck = !(s.get().getStatus().equals("Started") || s.get().getStatus().equals("Running"));
			if(datePreCheck) {
				s.get().setDate(null);
				return Optional.of(srep.save(s.get())).filter(sr -> null != sr.getDate()).isEmpty();
			}
			else {				
				logger.error("The screening is in '{}' state. Thus, it cannot be removed from the theatre.",
						s.get().getStatus());
				return false;					
			}
		}
		else {			
			logger.error("The screening is not present in the database, Please check and verify!\n {}", screening);
			return false;
		}
	}
	
	
	//Delete Screening
	public boolean deleteScreening(Screening screening) {
		Optional<Screening> s = srep.findById(screening.getId());
		if(s.isPresent()) {
			boolean theatreRemoved = deleteScreeningTheatre(s.get());
			boolean movieRemoved = deleteScreeningMovie(s.get());
			boolean showTimeRemoved = deleteScreeningShowTime(s.get());
			boolean dateRemoved = deleteScreeningDate(s.get());
			boolean statusPreCheck = !(s.get().getStatus().equals("Started") || s.get().getStatus().equals("Running"));
			if(theatreRemoved && movieRemoved && showTimeRemoved && dateRemoved && statusPreCheck) {
				srep.delete(s.get());
				return srep.findById(screening.getId()).isEmpty();
			}
			else {
				logger.warn("Please check the below screening and verify!\n {}",s.get());
				return false;
			}
		}		
		else {			
			logger.error("The screening is not present in the database, Please check and verify!\n {}", screening);
			return false;
		}
	}
}
