package com.movieplan.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movieplan.entity.FilterSet;
import com.movieplan.entity.Search;
import com.movieplan.repository.FilterSetRepository;
import com.movieplan.repository.SearchRepository;
import com.movieplan.services.FilterSetService;
@Service
public class SearchService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SearchRepository srep;

	@Autowired
	FilterSetRepository fsrep;

	@Autowired
	FilterSetService fsservice;

	// CREATE METHODS
	public boolean addSearch(Search c) {
		List<Search> esr = getParticularSearch(c.getUserId(), c.getSearchType(), c.getSearchKeyword(), c.getSortField(),
				c.getSortOrder(), c.getFilterSet());
		if (esr != null && esr.size() > 0) {
			logger.error("Search with id->{} is already present! Please check the search table and try again!",
					c.getId());
			return false;
		} 
		else if(c.getFilterSet()==null) {
			c.setFilterSet(null);
			return srep.save(c)!=null;
		}
		else {
			List<FilterSet> fsr = fsservice.getParticularFilterSet(c.getFilterSet());
			if (fsr == null || fsr.size() == 0) {
				FilterSet fssaved = fsservice.addFilterSet(c.getFilterSet());
				c.setFilterSet(fssaved);
			} else if (fsr.size() > 1) {
				logger.error("Multiple filtersets are present with these values->{}", c.getFilterSet());
				return false;
			}

			return srep.save(c)!=null;
		}
	}

	// READ METHODS
	public Search getSearchById(int id) {
		Optional<Search> s = srep.findById(id);
		if (s.isEmpty()) {
			logger.error("No such search entity present with id -> {}.Please check search table and try again!", id);
			return null;
		} else {
			return s.get();
		}
	}

	public List<Search> getSearchesByUserId(String userId) {
		List<Search> s = srep.findByUserId(userId);
		return s;

	}

	public List<Search> getThreeFieldSearch(String userId, String searchType, String searchKeyword) {
		return srep.findByUserIdAndSearchTypeAndSearchKeyword(userId, searchType, searchKeyword);
	}

	public List<Search> getAllSearches() {
		return srep.findAll();
	}

	public List<Search> getParticularSearch(String userId, String searchType,
			String SearchKeyword, String sortField,
			String sortValue, FilterSet filterSet) {
		List<FilterSet> pfs = fsservice.getParticularFilterSet(filterSet);
		if(filterSet==null) {
			List<Search> asr = srep.findAll().stream().filter(
					sr -> sr.getUserId().equals(userId)
					&& sr.getSearchType().equals(searchType)
					&& sr.getSearchKeyword().equals(SearchKeyword)
					&& sr.getSortField().equals(sortField)
					&& sr.getSortOrder().equals(sortValue)
					).collect(Collectors.toList());
			System.out.println(asr);
			return asr;
		}
		else if (pfs != null && pfs.size() == 1) {
			List<Search> asr = srep.findAll().stream()
					.filter(sr -> sr.getUserId().equals(userId) 
							&& sr.getSearchType().equals(searchType)
							&& sr.getSearchKeyword().equals(SearchKeyword)
							&& sr.getSortField().equals(sortField)
							&& sr.getSortOrder().equals(sortValue) 
							&& matchFilterSets(sr.getFilterSet(),filterSet)
							)
					.collect(Collectors.toList());
			System.out.println(asr);
			return asr;
		} else {
			logger.error(
					"There are multiple filtersets or no filtersets with the following values provided in the filterset!"
							+ "Please check the database and verify!!->{}",
					filterSet);
			return null;

		}
	}

	/*
	 * //UPDATE METHODS public boolean updateSearchFilters(Search s, List<Filter> f)
	 * { Optional<Search> sop = srep.findById(s.getId()); if(sop.isPresent()) {
	 * s.setFilters(f); return !Optional.of(srep.save(s)).isEmpty(); } else {
	 * logger.
	 * error("No search is present with id->{}. Please check the database and try again!"
	 * ,s.getId()); return false; } }
	 * 
	 * 
	 */

	// UPDATE METHODS

	public boolean updateSearch(Search s) {
		Optional<Search> esearch = srep.findById(s.getId());
		if (esearch.isPresent()) {
			Search es = esearch.get();
			es.setSearchType(s.getSearchType());
			es.setSearchKeyword(s.getSearchKeyword());
			es.setSortField(s.getSortField());
			es.setSortOrder(s.getSortOrder());
			es.setUserId(s.getUserId());
			List<FilterSet> efs = fsservice.getParticularFilterSet(s.getFilterSet());
			if (efs != null && efs.size() > 1) {
				logger.error("Multiple filtersets are present with these values -> {}", s.getFilterSet());
				return false;
			} else if (efs == null || efs.size() == 0) {
				//If No such filtersets present, save in the database and use it
				FilterSet fssaved = fsservice.addFilterSet(es.getFilterSet());
				if (fssaved != null) {
					es.setFilterSet(fssaved);
				} else {
					logger.error("FilterSet NOT Saved Successfully!!"
							+ " Please check the filterset and verify->{}",
							fssaved);
					return false;
				}
			} else if (efs != null && efs.size() == 1) {
				//If the very filterset is present, use it
				es.setFilterSet(efs.get(0));
			}

			return srep.save(es) != null;
		} else {
			logger.error("The search with Id -> {} does not exist!"
					+ " Pleae check the database and verify!!", s.getId());
			return false;
		}
	}

	// DELETE METHODS

	public boolean deleteSearch(Search s) {
		Optional<Search> esearch = srep.findById(s.getId());
		if (esearch.isPresent()) {
			srep.deleteById(s.getId());
			return srep.findById(s.getId()).filter(sr -> sr.getId() == s.getId()).isEmpty();
		} else {
			logger.error("The search with Id -> {} does not exist!"
					+ " Pleae check the database and verify!!", s.getId());
			return false;
		}
	}
	
	//UTILITY METHOD
	private boolean matchFilterSets(FilterSet itr,FilterSet ref) {
		if(itr==null) {
			return ref==null;
		}
		else if(ref==null) {
			return false;
		}
		return filterMatcher(itr.getTitle(),ref.getTitle())
				&& filterMatcher(itr.getLanguage(),ref.getLanguage())
				&& itr.getAmountMax() == ref.getAmountMax()
				&& filterMatcher(itr.getGenre(),ref.getGenre())
				&& filterMatcher(itr.getShowName(),ref.getShowName())
				&& (itr.getDate()!=null ? itr.getDate().equals(ref.getDate())
						: itr.getDate() == ref.getDate()) 
				&& filterMatcher(itr.getCity(),ref.getCity())
				&& filterMatcher(itr.getState(),ref.getState());
	}
	
	
	public boolean filterMatcher(String itr,String ref){
		if(itr==null) {
			return ref==null;
		}
		else {
			return ref==null? false:itr.equals(ref);
		}
	}

}
