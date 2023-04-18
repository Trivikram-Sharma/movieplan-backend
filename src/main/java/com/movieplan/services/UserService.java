package com.movieplan.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.User;
import com.movieplan.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public User signIn(String userName, String password) {
		Optional<User> userOptional = userRepository.findById(userName);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			
			if(user.getPassword().equals(password)) {
				user.setStatus("Active");
				userRepository.save(user);
				logger.info("User ->  {} is logged in successfully. State -> {}", user.getUserName(), user.getStatus());
				return user;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	public User signOut(String userName, String password) {
		Optional<User> userOptional = userRepository.findById(userName);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			
			if(user.getPassword().equals(password)) {
				user.setStatus("inactive");
				userRepository.save(user);
				logger.info("User ->  {} is logged out successfully. State -> {}", user.getUserName(), user.getStatus());
				return user;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public boolean signUp(String userName, String password, String fullName, String email) {
		Optional<User> userOptional = userRepository.findById(userName);
		if(userOptional.isPresent()) {
			logger.error("User {} is already present and registered! Please try to login/change the userName!", userName);
			return false;
		}
		else {
			User user = new User();
			user.setStatus("inactive");
			user.setUserName(userName);
			user.setPassword(password);
			user.setFullName(fullName);
			user.setEmail(email);
			return userRepository.save(user)!=null;
		}
	}
	
	public boolean changePassword(String userName, String oldPassword, String newPassword) {
		Optional<User> userOptional = userRepository.findById(userName);
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			if(user.getPassword().equals(oldPassword)) {
				user.setPassword(newPassword);
				return userRepository.save(user)!=null;
			}
			else {
				logger.error("The existing password provided is incorrect! Please check the existing password and try again!");
				return false;
			}
		}
		else {
			logger.warn("The {} user is not registered!",userName);
			return false;
		}
	}
	
	public User getUserWithId(String userName) {
		return userRepository.findById(userName).orElse(null);
	}
	
}
