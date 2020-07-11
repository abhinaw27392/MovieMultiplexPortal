package com.lti.project.movie.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lti.project.movie.document.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, String> {

	List<Movie> findByMovieNameIgnoreCaseStartsWith(String searchString);

}
