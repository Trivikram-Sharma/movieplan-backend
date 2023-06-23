package com.movieplan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movieplan.entity.Address;
import com.movieplan.services.AddressService;

@RestController
@RequestMapping("/api/address")
public class AddressController {

	@Autowired
	private AddressService aservice;

	@GetMapping("/{doorNo}")
	public Address getAddress(@PathVariable("doorNo") String doorNo) {
		return aservice.getAddressWithBuilding(doorNo);
	}

	@GetMapping("/search/")
	public ResponseEntity<List<Address>> getAllAddresses() {
		List<Address> result = aservice.getAllAddresses();
		if (null == result || result.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@GetMapping("/search/street")
	public ResponseEntity<List<Address>> getAllAddressesWithStreet(@RequestParam(required = true) String street) {
		List<Address> result = aservice.getAddressesWithStreet(street);
		if (null == result || result.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@GetMapping("/search/area")
	public ResponseEntity<List<Address>> getAllAddressesWithArea(@RequestParam(required = true) String area) {
		List<Address> result = aservice.getAddressesWithArea(area);
		if (null == result || result.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@GetMapping("/search/city")
	public ResponseEntity<List<Address>> getAllAddressesInCity(@RequestParam(required = true) String city) {
		List<Address> result = aservice.getAddressesInCity(city);
		if (null == result || result.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@GetMapping("/search/state")
	public ResponseEntity<List<Address>> getAllAddresses(@RequestParam(required = true) String state) {
		List<Address> result = aservice.getAddressesInState(state);
		if (null == result || result.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@GetMapping("/search/country")
	public ResponseEntity<List<Address>> getAddressesWithCountry(@RequestParam(required = true) String country) {
		List<Address> result = aservice.getAddressesInCountry(country);
		if (result.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@GetMapping("/search/pincode")
	public ResponseEntity<List<Address>> getAllAddressesWithPinCode(@RequestParam(required = true) String pincode) {
		List<Address> result = aservice.getAddressesWithPincode(pincode);
		if (null == result || result.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@GetMapping("/search/street/{streetName}")
	public ResponseEntity<List<Address>> getAllAddressesHavingStreetName(
			@PathVariable("streetName") String streetName) {
		List<Address> result = aservice.getAddressesHavingStreet(streetName);
		if (null == result || result.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@GetMapping("/search/area/{areaName}")
	public ResponseEntity<List<Address>> getAllAddressesHavingArea(@PathVariable("areaName") String areaName) {
		List<Address> result = aservice.getAddressesHavingArea(areaName);
		if (null == result || result.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@PostMapping("/add")
	public boolean addAddress(@RequestBody Address address) {
		return aservice.addAddress(address);
	}

	@PatchMapping("/update/{doorNo}")
	public Address updateAddress(@RequestBody Address address, @PathVariable("doorNo") String doorNo) {
		boolean addressPreCheck = address.getBuilding().equals(doorNo);
		Address oldaddress = aservice.getAddressWithBuilding(doorNo);
		if (addressPreCheck) {
			boolean updatedStreet = aservice.updateStreet(oldaddress, address.getStreet());
			boolean updatedArea = aservice.updateArea(oldaddress, address.getArea());
			boolean updatedCity = aservice.updateCity(oldaddress, address.getCity());
			boolean updatedState = aservice.updateState(oldaddress, address.getState());
			boolean updatedCountry = aservice.updateCountry(oldaddress, address.getCountry());
			boolean updatedPinCode = aservice.updatePinCode(oldaddress, address.getPincode());
			if (updatedStreet && updatedArea && updatedCity && updatedState && updatedCountry && updatedPinCode) {
				return address;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	@PatchMapping("/update/street/{doorNo}")
	public boolean updateAddressStreet(@PathVariable("doorNo") String doorNo, @RequestParam(required = true) String street) {
		return aservice.updateStreet(aservice.getAddressWithBuilding(doorNo), street);
	}
	@PatchMapping("/update/area/{doorNo}")
	public boolean updateAddressArea(@PathVariable("doorNo") String doorNo, @RequestParam(required = true) String area) {
		return aservice.updateArea(aservice.getAddressWithBuilding(doorNo), area);
	}
	@PatchMapping("/update/city/{doorNo}")
	public boolean updateAddressCity(@PathVariable("doorNo") String doorNo, @RequestParam(required = true) String city) {
		return aservice.updateCity(aservice.getAddressWithBuilding(doorNo), city);
	}
	@PatchMapping("/update/state/{doorNo}")
	public boolean updateAddressState(@PathVariable("doorNo") String doorNo, @RequestParam(required = true) String state) {
		return aservice.updateStreet(aservice.getAddressWithBuilding(doorNo), state);
	}
	@PatchMapping("/update/country/{doorNo}")
	public boolean updateAddressCountry(@PathVariable("doorNo") String doorNo, @RequestParam(required = true) String country) {
		return aservice.updateCountry(aservice.getAddressWithBuilding(doorNo), country);
	}
	@PatchMapping("/update/pincode/{doorNo}")
	public boolean updateAddressPinCode(@PathVariable("doorNo") String doorNo, @RequestParam(required = true) String pincode) {
		return aservice.updatePinCode(aservice.getAddressWithBuilding(doorNo), pincode);
	}

	@DeleteMapping("/delete/{doorNo}")
	public boolean deleteAddress(@PathVariable("doorNo") String doorNo) {
		return aservice.deleteAddress(aservice.getAddressWithBuilding(doorNo));
	}
	
	@DeleteMapping("/delete/street")
	public boolean deleteAddressesWithStreet(@RequestParam(required = true) String street) {
		List<Address> addressesToBeDeleted = aservice.getAddressesWithStreet(street);
		return addressesToBeDeleted.stream().map(a -> aservice.deleteAddress(a)).filter(b -> b == false).count() == 0;
	}
	@DeleteMapping("/delete/area")
	public boolean deleteAddressesWithArea(@RequestParam(required = true) String area) {
		List<Address> addressesToBeDeleted = aservice.getAddressesWithArea(area);
		return addressesToBeDeleted.stream().map(a -> aservice.deleteAddress(a)).filter(b -> b == false).count() == 0;
		
	}
	@DeleteMapping("/delete/city")
	public boolean deleteAddressesWithCity(@RequestParam(required = true) String city) {
		List<Address> addressesToBeDeleted = aservice.getAddressesInCity(city);
		return addressesToBeDeleted.stream().map(a -> aservice.deleteAddress(a)).filter(b -> b == false).count() == 0;
		
	}
	@DeleteMapping("/delete/state")
	public boolean deleteAddressesWithState(@RequestParam(required = true) String state) {
		List<Address> addressesToBeDeleted = aservice.getAddressesInState(state);
		return addressesToBeDeleted.stream().map(a -> aservice.deleteAddress(a)).filter(b -> b == false).count() == 0;
		
	}
	@DeleteMapping("/delete/country")
	public boolean deleteAddressesWithCountry(@RequestParam(required = true) String country) {
		List<Address> addressesToBeDeleted = aservice.getAddressesInCountry(country);
		return addressesToBeDeleted.stream().map(a -> aservice.deleteAddress(a)).filter(b -> b == false).count() == 0;	
	}
	@DeleteMapping("/delete/pincode")
	public boolean deleteAddressesWithPinCode(@RequestParam(required = true) String pincode) {
		List<Address> addressesToBeDeleted = aservice.getAddressesWithPincode(pincode);
		return addressesToBeDeleted.stream().map(a -> aservice.deleteAddress(a)).filter(b -> b == false).count() == 0;	
	}
}
