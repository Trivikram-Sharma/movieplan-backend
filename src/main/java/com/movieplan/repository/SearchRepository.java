package com.movieplan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieplan.entity.Search;

public interface SearchRepository extends JpaRepository<Search, Integer>{

		public List<Search> findByUserId(String userId);
		
		public List<Search> findBySearchTypeAndSearchKeyword(String searchType, String searchKeyword);
		
//		@Query("select s from search where"
//				+ " s.userId = :userId and s.searchType = :searchType and"
//				+ " s.searchKeyword = :searchKeyword and s.sortField = :sortField and sortOrder = :sortOrder")
//		public Search getParticularSearch(@Param("userId") String userId, 
//				@Param("searchType") String searchType,
//				@Param("searchKeyword") String searchKeyword,
//				@Param("sortField") String sortField,
//				@Param("sortOrder") String sortOrder,
//				@Param("filters") Integer filters);
		public List<Search> findByUserIdAndSearchTypeAndSearchKeyword(String userId, String searchType, String searchKeyword);
		
		public Optional<Search> findById(Integer id);
}
