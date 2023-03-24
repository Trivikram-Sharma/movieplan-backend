package com.movieplan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.Admin;
import com.movieplan.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adservice;

	@PostMapping("/login")
	public String adminLogin(@RequestBody Admin admin) {
		Admin a = adservice.signIn(admin.getAdminUserName(), admin.getAdminPassword());
		if (null != a && a.getStatus().equals("Active")) {
			return "Hi " + a.getAdminUserName() + "! Welcome! Your Login is Successful!";
		} else {
			return "Username or Password is incorrect. Please try again.";
		}
	}

	@PostMapping("/logout")
	public String adminLogOut(@RequestBody Admin admin) {
		Admin a = adservice.signOut(admin.getAdminUserName(),admin.getAdminPassword());
		if(null!=a && a.getStatus().equals("inactive")) {
			return "Logged Out Successfully!";
		}
		else {
			return "Logout Failed! Please try again after sometime!";
		}
	}

}
