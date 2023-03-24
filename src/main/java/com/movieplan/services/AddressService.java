package com.movieplan.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.Address;
import com.movieplan.repository.AddressRepository;

@Service
public class AddressService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AddressRepository adrepo;

	/************ CREATE METHODS ************/

	// add Address
	public boolean addAddress(Address address) {
		Optional<Address> addr = adrepo.findById(address.getBuilding());
		if (addr.isPresent()) {
			logger.warn(
					"The address with building/door No {} already existis! Please check the database and verify the validity of the following address\n {}",
					address.getBuilding(), address);
			return false;
		} else {
			adrepo.save(address);
			if (adrepo.findById(address.getBuilding()).isPresent()) {
				logger.info("Below Address saved successfully!\n {}", adrepo.findById(address.getBuilding()));
				return true;
			} else {
				logger.error(
						"The address is not saved successfully. Please check the database on the below address and verify\n {}",
						address);
				return false;
			}
		}
	}

	/********** READ METHODS ***********/

	//Get All Addresses
	public List<Address> getAllAddresses(){
		return adrepo.findAll();
	}
	
	// Get the address with a particular building/ door No
	public Address getAddressWithBuilding(String building) {
		return adrepo.findById(building).orElse(null);
	}

	// Get the addresses with a particular street

	public List<Address> getAddressesWithStreet(String street) {
		return adrepo.findByStreet(street);
	}

	// Get the addresses containing a particular street name
	public List<Address> getAddressesHavingStreet(String street) {
		return adrepo.findContainingStreet(street);
	}

	// Get the addresses with a particular area

	public List<Address> getAddressesWithArea(String area) {
		return adrepo.findByArea(area);
	}

	// Get the addresses containing a particular area name
	public List<Address> getAddressesHavingArea(String area) {
		return adrepo.findContainingArea(area);
	}

	// Get addresses in a particular city
	public List<Address> getAddressesInCity(String city) {
		return adrepo.findByCity(city);
	}

	// Get addresses in a particular state
	public List<Address> getAddressesInState(String state) {
		return adrepo.findByState(state);
	}

	// Get addresses in a particular country
	public List<Address> getAddressesInCountry(String country) {
		return adrepo.findByCountry(country);
	}

	// Get address with a pincode
	public List<Address> getAddressesWithPincode(String pincode) {
		return adrepo.findByPincode(pincode);
	}

	/**************** UPDATE METHODS ****************/
	public boolean updateStreet(Address address, String street) {
		Optional<Address> addr = adrepo.findById(address.getBuilding());
		if (addr.isPresent()) {
			addr.get().setStreet(street);
			adrepo.save(addr.get());
			if (adrepo.findById(address.getBuilding()).isPresent()
					&& adrepo.findById(address.getBuilding()).get().getStreet().equals(street)) {
				return true;
			} else {
				logger.warn(
						"Street of the address is not updated! Please check the following address and street and verify in the database!\n {}\n{}",
						addr.get(), street);
				return false;
			}
		} else {
			logger.warn("Below address is not present in the database. Please check and verify\n{}", address);
			return false;
		}
	}

	public boolean updateArea(Address address, String area) {
		Optional<Address> addr = adrepo.findById(address.getBuilding());
		if (addr.isPresent()) {
			addr.get().setArea(area);
			return adrepo.save(addr.get()) != null
					&& adrepo.findById(address.getBuilding()).get().getArea().equals(area);
		} else {
			logger.error("No such address present. Please check the below address in database and verify!\n{}",
					address);
			return false;
		}
	}

	public boolean updateCity(Address address, String city) {
		Optional<Address> addr = adrepo.findById(address.getBuilding());
		if (addr.isPresent()) {
			addr.get().setCity(city);
			return adrepo.save(addr.get()) != null
					&& adrepo.findById(address.getBuilding()).get().getCity().equals(city);
		} else {
			logger.error("Such address is not present. Please check below address in database and verify!\n{}",
					address);
			return false;
		}
	}

	public boolean updateState(Address address, String state) {
		Optional<Address> addr = adrepo.findById(address.getBuilding());
		if (addr.isPresent()) {
			addr.get().setState(state);
			return adrepo.save(addr.get()) != null
					&& adrepo.findById(address.getBuilding()).get().getState().equals(state);
		} else {
			logger.error("Such address is not present. Please check below address in database and verify!\n{}",
					address);
			return false;
		}
	}

	public boolean updateCountry(Address address, String country) {
		Optional<Address> addr = adrepo.findById(address.getBuilding());
		if (addr.isPresent()) {
			addr.get().setCountry(country);
			return adrepo.save(addr.get()) != null
					&& adrepo.findById(address.getBuilding()).get().getCountry().equals(country);
		} else {
			logger.error("Such address is not present. Please check below address in database and verify!\n{}",
					address);
			return false;
		}
	}

	public boolean updatePinCode(Address address, String pincode) {
		Optional<Address> addr = adrepo.findById(address.getBuilding());
		if (addr.isPresent()) {
			addr.get().setPincode(pincode);
			return adrepo.save(addr.get()) != null
					&& adrepo.findById(address.getBuilding()).get().getPincode().equals(pincode);
		} else {
			logger.error("Such address is not present. Please check below address in database and verify!\n{}",
					address);
			return false;
		}
	}

	/*************** DELETE METHODS *******/
	public boolean deleteAddress(Address address) {
		adrepo.delete(address);
		if (adrepo.findById(address.getBuilding()).isPresent()) {
			logger.warn(
					"The address is still present in the database! Thus delete failed. The address is as shown below \n {}",
					adrepo.findById(address.getBuilding()).get());
			return false;
		}
		else {
			logger.info("Address deleted successfully!");
			return true;
		}
	}
}
