package com.movieplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieplan.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String>{

}
