package com.movieplan.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.movieplan.entity.ShowTimes;

public interface ShowTimesRepository extends JpaRepository<ShowTimes, Integer>{

	public List<ShowTimes> findByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);
	public List<ShowTimes> findByShowName(String showName);
	@Query("Select st from ShowTimes st where st.showName =:showName and st.startTime =:startTime and st.endTime =:endTime ")
	public List<ShowTimes> findParticularShowTimes(String showName, LocalTime startTime, LocalTime endTime);
	
}
