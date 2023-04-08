package com.movieplan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.User;
import com.movieplan.services.AddressService;
import com.movieplan.services.MovieService;
import com.movieplan.services.ScreeningService;
import com.movieplan.services.ShowTimeService;
import com.movieplan.services.TheatreService;
import com.movieplan.services.UserService;
import com.util.ChangePasswordRecord;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private ScreeningService sservice;
	
	@Autowired
	private MovieService mservice;
	
	
	@Autowired
	private TheatreService thservice;
	
	@Autowired
	private AddressService aservice;


	@Autowired
	private ShowTimeService stservice;

	
	
	//POST APIs
	@PostMapping("/signUp")
	public boolean signUp(@RequestBody User user) {
		return userService.signUp(user.getUserName(), user.getPassword());
	}
	
	@PostMapping("/signIn")
	public User signIn(@RequestBody User user) {
		return userService.signIn(user.getUserName(), user.getPassword());
	}
	
	@PatchMapping("/changePassword")
	public boolean changePassword(@RequestBody ChangePasswordRecord changePasswordRecord) {
		return userService.changePassword(changePasswordRecord.getUserName(), changePasswordRecord.getOldPassword(), changePasswordRecord.getNewPassword());
	}
	
	@PatchMapping("/signOut")
	public boolean signOut(@RequestParam(required = true) String userName) {
		return userService.signOut(userName, userService.getUserWithId(userName).getPassword())!=null;
	}
	
}
