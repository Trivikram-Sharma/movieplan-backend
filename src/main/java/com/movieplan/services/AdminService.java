package com.movieplan.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.Admin;
import com.movieplan.repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public Admin signIn(String adminUserName, String adminPassword) {
		Optional<Admin> adminOptional = adminRepository.findById(adminUserName);
		if(adminOptional.isPresent()) {
			Admin a = adminOptional.get();
			if(a.getAdminPassword().equals(adminPassword) && a.getStatus().equals("inactive")) {
				a.setStatus("Active");
				adminRepository.save(a);
				logger.info("Admin -> {} has been successfully Signed In, state -> {}", a.getAdminUserName(), a.getStatus());
				return a;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public Admin signOut(String adminUserName, String adminPassword) {
		Optional<Admin> adminOptional = adminRepository.findById(adminUserName);
		if(adminOptional.isPresent()) {
			Admin a = adminOptional.get();
			if(a.getAdminPassword().equals(adminPassword) && a.getStatus().equals("Active")) {
				a.setStatus("inactive");
				adminRepository.save(a);
				logger.info("Admin -> {} has been successfully Signed Out, state -> {}", a.getAdminUserName(), a.getStatus());
				return a;
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
