package com.movieplan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieplan.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer>{

	public List<Genre> findByName(String name);
	
}
