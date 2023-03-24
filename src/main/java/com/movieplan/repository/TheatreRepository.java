package com.movieplan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieplan.entity.Address;
import com.movieplan.entity.Theatre;

public interface TheatreRepository extends JpaRepository<Theatre,Integer>{

	
	List<Theatre> findByAddress(Address address);
	
	List<Theatre> findByName(String name);
	
	List<Theatre> findByScreens(int screens);
	
}
