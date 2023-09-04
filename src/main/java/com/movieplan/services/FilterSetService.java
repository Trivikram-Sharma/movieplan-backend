package com.movieplan.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.FilterSet;
import com.movieplan.repository.FilterSetRepository;
import com.movieplan.repository.SearchRepository;

@Service
public class FilterSetService {

	@Autowired
	FilterSetRepository fsrep;
	
	@Autowired
	SearchRepository srep;
	
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	
	//READ METHODS
	
	public List<FilterSet> getParticularFilterSet(FilterSet filterSet){
		if(filterSet==null) {
			return null;
		}
		
		return fsrep.findAll().stream().filter(
				fs -> fs.getAmountMax() == filterSet.getAmountMax()
					&& filterMatcher(fs.getCity(),filterSet.getCity())
					&& (fs.getDate()!=null ? fs.getDate().equals(filterSet.getDate())
							: fs.getDate() == filterSet.getDate())
					&& filterMatcher(fs.getTitle(),filterSet.getTitle())
					&& filterMatcher(fs.getLanguage(),filterSet.getLanguage())
					&& filterMatcher(fs.getGenre(),filterSet.getGenre())
					&& filterMatcher(fs.getShowName(),filterSet.getShowName())
					&& filterMatcher(fs.getState(),filterSet.getState())
				).collect(Collectors.toList());
	}
	
	public FilterSet getFilterSetById(int id) {
		Optional<FilterSet> result = fsrep.findById(id);
		if(result.isPresent()) {
			return result.get();
		}
		else {
			return null;
		}
	}
	public List<FilterSet> getAllFilterSets(){
		return fsrep.findAll();
	}
	
	//CREATE METHODS
	
		public FilterSet addFilterSet(FilterSet filterSet) {
			FilterSet result = null;
			List<FilterSet> existingResults = getParticularFilterSet(filterSet);
			if(existingResults==null ||(existingResults!=null && existingResults.size() == 0)) {
				result = fsrep.save(filterSet);
				if(result.getId() != 0) {
					return result;
				}
				else {
					logger.error("FilterSet NOT Saved successfully!-> {}",filterSet);
					return null;
				}
			}
			else if(existingResults!=null && existingResults.size() > 1) {
				logger.error("Multiple filtersets present with the following values!! "
						+ "Please check the database and verify!"
						+ "->{}",filterSet);
				return null;
			}
			else if(existingResults.size()==1) {
				logger.warn("The FilterSet already exists!! Please check the database and verify!! -. {}",filterSet);
				return filterSet;
			}
			else {
				logger.error("Unknown error! Please check filterset table and verify!->{}",filterSet);
				return null;
			}
		}
		
	//UPDATE METHODS
		
	public boolean updateFilterSet(int filterSetId,FilterSet filterSet) {
		Optional<FilterSet> existingFilterSet = fsrep.findById(filterSetId);
		if(existingFilterSet.isPresent()) {
			FilterSet existingResult = existingFilterSet.get();
			existingResult.setAmountMax(filterSet.getAmountMax());
			existingResult.setCity(filterSet.getCity());
			existingResult.setDate(filterSet.getDate());
			existingResult.setGenre(filterSet.getGenre());
			existingResult.setLanguage(filterSet.getLanguage());
			existingResult.setShowName(filterSet.getShowName());
			existingResult.setTitle(filterSet.getTitle());
			existingResult.setState(filterSet.getState());
			return fsrep.save(existingResult)!=null;
		}
		else {
			logger.error("The FilterSet with the Id {} is not present! Please check the database and verify!",filterSet.getId());
			return false;
		}
	}
	
	//DELETE METHODS
	
	public boolean deleteFilterSet(FilterSet filterSet) {
		Optional<FilterSet> fsop = fsrep.findById(filterSet.getId());
		if(fsop.isPresent()) {
			fsrep.deleteById(filterSet.getId());
			return fsrep.findById(filterSet.getId()).filter(x -> x.getId()==filterSet.getId()).isEmpty();
		}
		else {
			logger.error("The FilterSet does not exist. Please check database and verify!!->{}",filterSet);
			return false;
		}
		
	}
	
	
	//UTILITY METHODS
	public boolean filterMatcher(String itr,String ref){
		if(itr==null) {
			return ref==null;
		}
		else {
			return ref==null? false:itr.equals(ref);
		}
	}
}
