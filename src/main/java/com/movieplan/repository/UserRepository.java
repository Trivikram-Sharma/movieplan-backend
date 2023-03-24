package com.movieplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieplan.entity.User;

public interface UserRepository extends JpaRepository<User, String>{

}
