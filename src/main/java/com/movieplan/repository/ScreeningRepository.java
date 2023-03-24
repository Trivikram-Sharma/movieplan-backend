package com.movieplan.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieplan.entity.Movie;
import com.movieplan.entity.Screening;
import com.movieplan.entity.ShowTimes;
import com.movieplan.entity.Theatre;

public interface ScreeningRepository extends JpaRepository<Screening,Integer>{

	List<Screening> findByTheatre(Theatre theatre);
	List<Screening> findByMovie(Movie movie);
	List<Screening> findByShowTime(ShowTimes showTime);
	List<Screening> findByDate(LocalDate date);
}
