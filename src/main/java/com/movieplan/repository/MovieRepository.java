package com.movieplan.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.movieplan.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, String> {

	@Query("select m from Movie m where m.title =:title and m.language = :language and m.description=:description and m.releaseDate =:releaseDate")
	public List<Movie> getParticularMovie(@Param("title") String title, @Param("language") String language, @Param("description") String description,
			@Param("releaseDate") LocalDate releaseDate);

	public List<Movie> findByTitle(String title);

	public List<Movie> findByLanguage(String language);

	@Query("select m from Movie m where m.price >=:lprice and m.price <=:hprice")
	public List<Movie> getMoviesInPriceRange(@Param("hprice") int hprice, @Param("lprice") int lprice);

	@Query("select m from Movie m where m.title like ':name'")
	public List<Movie> getMoviesContainingName(@Param("name") String name);

	public List<Movie> findByStatus(String status);
	
	public List<Movie> findByReleaseDate(LocalDate date);
	
	public List<Movie> findAllById(String id);
	
	public List<Movie> findByPrice(int price);
}
