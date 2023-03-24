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
}
