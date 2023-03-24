package com.movieplan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.services.AddressService;
import com.movieplan.services.MovieService;
import com.movieplan.services.ScreeningService;
import com.movieplan.services.ShowTimeService;
import com.movieplan.services.TheatreService;
import com.movieplan.services.UserService;

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
//	@PostMapping("/signUp")
//	public boolean signUp(@RequestBody ) {
//		
//	}
}
