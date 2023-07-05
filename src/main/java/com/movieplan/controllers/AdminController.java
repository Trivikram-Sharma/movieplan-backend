package com.movieplan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.Admin;
import com.movieplan.services.AdminService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

	@Autowired
	private AdminService adservice;

	@PostMapping("/login")
	public Admin adminLogin(@RequestBody Admin admin) {
		Admin a = adservice.signIn(admin.getAdminUserName(), admin.getAdminPassword());
		if (null != a && a.getStatus().equals("Active")) {
			return a;
		} else {
			return null;
		}
	}

	@PostMapping("/logout")
	public Admin adminLogOut(@RequestBody Admin admin) {
		Admin a = adservice.signOut(admin.getAdminUserName(),admin.getAdminPassword());
		if(null!=a && a.getStatus().equals("inactive")) {
			return a;
		}
		else {
			return null;
		}
	}

}
