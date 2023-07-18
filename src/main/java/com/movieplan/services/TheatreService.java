package com.movieplan.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.Address;
import com.movieplan.entity.Screening;
import com.movieplan.entity.Theatre;
import com.movieplan.repository.AddressRepository;
import com.movieplan.repository.ScreeningRepository;
import com.movieplan.repository.TheatreRepository;

@Service
public class TheatreService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TheatreRepository trep;
	@Autowired
	private AddressRepository adrep;

	@Autowired
	private ScreeningRepository srep;

	/*********** CREATE METHODS **********/
	public boolean addTheatre(Theatre theatre) {
		List<Theatre> theatres = trep.findByAddress(theatre.getAddress());
		if (theatres.isEmpty()) {
			return trep.save(theatre) != null;
		} else {
			logger.error(
					"There is already a theatre at the below address! \n{}\n Please check the below theatre and above address and verify \n {}",
					theatre.getAddress(), theatre);
			return false;
		}
	}

	/************** READ METHODS ***********/

	public Theatre findTheatreById(int id) {
		return trep.findById(id).get();
	}

	public List<Theatre> getAllTheatres() {
		return trep.findAll();
	}
	public List<Theatre> getTheatresWithName(String name) {
		return trep.findByName(name);
	}

	public List<Theatre> getTheatresWithScreens(int screens) {
		return trep.findByScreens(screens);
	}

	public List<Theatre> getTheatresWithAddress(Address address) {
		return trep.findByAddress(address);
	}

	/***************** UPDATE METHODS *****************/

	public boolean updateTheatreName(String name, Theatre theatre) {
		Optional<Theatre> t = trep.findById(theatre.getId());
		if (t.isPresent()) {
			t.get().setName(name);
			return Optional.of(trep.save(t.get())).filter(tr -> tr.getName().equals(name)).isPresent();

		} else {
			logger.error("The below theatre is not present in the database. Please check and verify!\n {}", theatre);
			return false;
		}
	}

	// Update no of screens in the theatre
	public boolean updateScreens(Theatre theatre, int screens) {
		Optional<Theatre> tr = trep.findById(theatre.getId());
		if (tr.isPresent()) {
			if (screens > tr.get().getScreens()) {
				tr.get().setScreens(screens);
				return Optional.of(trep.save(tr.get())).filter(t -> t.getScreens() == screens).isPresent();
			} else if (screens < tr.get().getScreens()) {
				if (tr.get().getScreenings().size() < screens) {
					tr.get().setScreens(screens);
					return Optional.of(trep.save(tr.get()))
							.filter(t -> t.getScreens() == screens && t.getScreenings().size() < t.getScreens())
							.isPresent();
				} else {
					logger.error("The number of screenings scheduled in the theatre is {}, which is more than {}."
							+ " Thus, please check the screenings of the theatre and verify, before modifying the no of screens\n {}",
							tr.get().getScreenings(), screens, tr.get());
					return false;
				}
			} else {
				logger.warn(
						"The number of screens is already {}. Please check the theatre in the database and verify \n {}",
						screens, tr.get());
				return false;
			}
		} else {
			logger.error("Below theatre is not present. Please check the theatre and verify\n {}", theatre);
			return false;
		}
	}

	// Update address of the theatre
	public boolean updateTheatreAddress(Theatre theatre, Address address) {
		Optional<Theatre> tr = trep.findById(theatre.getId());
		Optional<Address> addr = adrep.findById(address.getBuilding());

		if (tr.isPresent()) {
			if (addr.isPresent()) {
				if (getTheatresWithAddress(address).isEmpty()) {
					tr.get().setAddress(address);
					return Optional.of(trep.save(tr.get())).filter(t -> t.getAddress().equals(address)).isPresent();
				} else {
					logger.error("{} \n The above address is already associated with some theatres."
							+ " Please check the above address in the theatre table and verify", address);
					return false;
				}

			} else {
				logger.error("The below address is not present in database. Please check and verify!\n {}", address);
				return false;
			}
		} else {
			logger.error("Below theatre is not present. Please check the theatre and verify\n {}", theatre);
			return false;
		}
	}

	// Update theatre screening
	public boolean updateTheatreScreening(Theatre theatre, Screening screening) {
		Optional<Theatre> tr = trep.findById(theatre.getId());
		if (tr.isPresent()) {
			if (tr.get().getScreenings().contains(screening)) {
				logger.warn("{} \n The above screening is already associated with below theatre \n {}", screening,
						tr.get());
				return false;
			} else {
				if (tr.get().getScreenings().size() + 1 <= tr.get().getScreens()) {
					if (srep.findById(screening.getId()).isPresent()) {
						tr.get().addScreening(srep.findById(screening.getId()).get());
						return Optional.of(trep.save(tr.get()))
								.filter(t -> t.getScreenings().contains(srep.findById(screening.getId()).get()))
								.isPresent();
					} else {
						logger.error("The below screening is not present in the database.Please check and verify\n {}",
								screening);
						return false;
					}

				} else {
					logger.error(
							"Theatre is fully occupied! Please check the screenings of the below theatre and verify \n {}",
							tr.get());
					return false;
				}
			}
		} else {
			logger.error("Below theatre is not present. Please check the theatre and verify\n {}", theatre);
			return false;
		}
	}

	/******************** DELETE METHODS ****************/
	// Remove all screens in the theatre
	public boolean removeTheatreScreens(Theatre theatre) {
		Optional<Theatre> tr = trep.findById(theatre.getId());
		if (tr.isPresent()) {
			if (tr.get().getScreenings().isEmpty()) {
				tr.get().setScreens(0);
				return Optional.of(trep.save(tr.get())).filter(t -> t.getScreens() == 0).isPresent();
			} else {
				logger.warn("{} screenings are scheduled in this theatre. Please check the database and verify!",
						tr.get().getScreenings().size());
				return false;
			}
		} else {
			logger.error("Below theatre is not present. Please check the theatre and verify\n {}", theatre);
			return false;
		}
	}

	// Remove the address of the theatre
	public boolean removeTheatreAddress(Theatre theatre) {
		Optional<Theatre> tr = trep.findById(theatre.getId());
		if (tr.isPresent()) {
			if ((tr.get().getScreenings().isEmpty() || tr.get().getScreenings() == null)
					&& tr.get().getScreens() == 0) {
				tr.get().setAddress(null);
				trep.save(tr.get());
				return trep.findById(theatre.getId()).filter(t -> t.getAddress() == null).isPresent();
			}else if(tr.get().getScreens() != 0) {
				boolean screensRemoved = removeTheatreScreens(tr.get());
				if(screensRemoved) {
					tr.get().setAddress(null);
					trep.save(tr.get());
					return trep.findById(theatre.getId()).filter(t -> t.getAddress() == null).isPresent();					
				}
				else {
					logger.error("{} Screens are present in the below theatre and failed to be removed! Please check the below theatre and verify!\n{}",tr.get().getScreens(),
							tr.get());
					return false;
				}
			} else {
				logger.warn(
						"{} screenings are scheduled in the theatre and {} screens are present in the theatre."
								+ " Please check the below theatre in database and verify.",
						tr.get().getScreenings().size(), tr.get().getScreens());
				return false;
			}
		} else {
			logger.error("Below theatre is not present. Please check the theatre and verify\n {}", theatre);
			return false;
		}
	}

	// Remove screening of the theatre
	public boolean removeTheatreScreening(Theatre theatre, Screening screening) {
		Optional<Theatre> tr = trep.findById(theatre.getId());
		Optional<Screening> sr = srep.findById(screening.getId());
		if (tr.isPresent()) {
			if (sr.isPresent()) {
				if (tr.get().getScreenings().contains(sr.get())) {
					tr.get().removeScreening(sr.get());
					return Optional.of(trep.save(tr.get())).filter(t -> !t.getScreenings().contains(sr.get()))
							.isPresent();
				} else {
					logger.warn(
							"{} \n The above screening is not associated with below theatre. Thus, screening removal failed. \n {}",
							screening, tr.get());
					return false;
				}
			} else {
				logger.warn("The below screening is not present in the database.\n {}", screening);
				return false;
			}
		} else {
			logger.error("Below theatre is not present. Please check the theatre and verify\n {}", theatre);
			return false;
		}

	}
	
	//Delete the theatre
	public boolean deleteTheatre(Theatre theatre) {
		Optional<Theatre> t = trep.findById(theatre.getId());
		if(t.isPresent()) {
			boolean precheck = t.filter(tr -> tr.getScreenings()== null || tr.getScreenings().isEmpty()).isPresent();
			if(precheck) {
				boolean addressRemoved = removeTheatreAddress(t.get());
				if(addressRemoved) {
					trep.delete(t.get());
					return trep.findById(theatre.getId()).isEmpty();
				}
				else {
					logger.error("Address Removal failed before deleting the theatre."
							+ "Thus, theatre deletion failed.\n Theatre -> {} \n Address -> {}",t.get(), t.get().getAddress());
					return false;
				}
			}
			else {
				logger.warn("{} active screenings are present in the below theatre. "
						+ "Please check the database and verify \n {}", t.get().getScreenings().size(), t.get());
				return false;
			}
		}
		else {
			logger.error("Below theatre is not present. Please check the theatre and verify\n {}", theatre);
			return false;
		}
	}
}
