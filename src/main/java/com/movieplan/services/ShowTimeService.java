package com.movieplan.services;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.ShowTimes;
import com.movieplan.repository.ShowTimesRepository;

@Service
public class ShowTimeService {

	@Autowired
	private ShowTimesRepository showTimeRepo;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	// Get ShowTime By Name
	public ShowTimes getShowTimeByName(String showName) {
		List<ShowTimes> showTimes = showTimeRepo.findByShowName(showName);
		if (showTimes.size() == 1) {
			return showTimes.get(0);
		} else {
			logger.warn(
					"More than one show Times are found with the show name {}. Please check the database and try again!",
					showName);
			return null;
		}
	}
	
	public List<ShowTimes> getAllShowTimes(){
		return showTimeRepo.findAll();
	}

	// Add Show Time
	public boolean addShowTime(String showName, String startTime, String endTime) {
		LocalTime startTimeValue = LocalTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_TIME);
		LocalTime endTimeValue = LocalTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_TIME);
		ShowTimes showTime = new ShowTimes(showName, startTimeValue, endTimeValue);
		List<ShowTimes> showTimes = showTimeRepo.findParticularShowTimes(showName, startTimeValue, endTimeValue);
		if (!showTimes.isEmpty()) {
			logger.warn(
					"Multiple shows are present with the showName -> {}," + "\n startTime -> {}," + " \n endTime -> {}."
							+ "There are {} shows with the above matching criteria."
							+ " \n Please check the database and try again!",
					showName, startTime, endTime, showTimes.size());
			return false;
		} else if (showTimes.size() == 1 && showTimes.get(0).getShowName().equals(showName)) {
			logger.error("This particular show already exists! Please check the database and try again!");
			return false;
		} else {
			showTimeRepo.save(showTime);
			return true;
		}
	}

	//Delete Show Time
	public boolean deleteShowTime(String showName, String startTime, String endTime) {
		LocalTime startTimeValue = LocalTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_TIME);
		LocalTime endTimeValue = LocalTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_TIME);
		List<ShowTimes> showTimes = showTimeRepo.findParticularShowTimes(showName, startTimeValue, endTimeValue);
		if (showTimes.size() > 1) {
			logger.warn(
					"Multiple shows are present with the showName -> {}," + "\n startTime -> {}," + " \n endTime -> {}."
							+ "There are {} shows with the above matching criteria."
							+ " \n Please check the database and try again!",
					showName, startTime, endTime, showTimes.size());
			return false;
		} else if (showTimes.size() == 1 && showTimes.get(0).getShowName().equals(showName)) {
			showTimeRepo.deleteById(showTimes.get(0).getId());
			if (showTimeRepo.findParticularShowTimes(showName, startTimeValue, endTimeValue).size() == 0) {
				return true;
			} else {
				logger.error(
						"Deletion of the show {},\nwith startTime -> {}," + " \n endTime -> {},"
								+ " \n is not successful! Please check the database and try again!",
						showName, startTime, endTime);
				return false;
			}
		} else {
			logger.warn("There are no shows with showName -> {}," + " \n startTime -> {}," + " \n endTime -> {}."
					+ "\nPlease check the database and verify.", showName, startTime, endTime);
			return false;
		}
	}
}
