package com.lti.project.movie.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lti.project.movie.document.MovieMultiplex;

@Repository
public interface MovieMultiplexRepository extends CrudRepository<MovieMultiplex, String> {

	List<MovieMultiplex> findByUserId(String userId);

	List<MovieMultiplex> findAllByMovieId(String id);

	List<MovieMultiplex> findAllByMultiplexId(String multiplexId);

	List<MovieMultiplex> findAllByMultiplexIdAndScreenName(String multiplexId, String screenName);

	void deleteAllByMovieId(String movieId);

	Optional<MovieMultiplex> findByMultiplexId(String multiplexId);

	void deleteAllByMultiplexId(String multiplexId);

}
