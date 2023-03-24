package com.movieplan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.movieplan.entity.Address;

public interface AddressRepository extends JpaRepository<Address,String>{

	List<Address> findByStreet(String street);
	
	@Query("select a from Address a where a.street like ':street'")
	List<Address> findContainingStreet(@Param("street") String street);

	List<Address> findByArea(String area);
	
	@Query("select a from Address a where a.area like ':area'")
	List<Address> findContainingArea(@Param("area") String area);
	
	List<Address> findByCity(String city);
	
	List<Address> findByState(String state);

	List<Address> findByCountry(String country);
	
	List<Address> findByPincode(String pincode);
	
}
