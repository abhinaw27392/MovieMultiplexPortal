package com.lti.project.movie.service;

import java.util.List;

import com.lti.project.movie.dto.MovieDto;
import com.lti.project.movie.dto.MovieMultiplexDetailsDto;
import com.lti.project.movie.dto.MovieMultiplexDto;
import com.lti.project.movie.dto.MovieMultiplexSearchResultDto;

public interface MovieService {

	List<MovieDto> getAllMovies();

	MovieDto addMovie(MovieDto movieDto, String userId);

	MovieDto getMovieById(String movieId);

	MovieDto updateMovie(String movieId, MovieDto movieDto, String userId);

	boolean deleteMovieById(String movieId, String userId);

	MovieMultiplexDetailsDto addMovieToMultiplex(String userId, MovieMultiplexDto movieMultiplexDto);

	List<MovieMultiplexDetailsDto> getAllAllotedMovieMultiplex();

	List<MovieMultiplexDetailsDto> getAllotedMovieMultiplexByUserId(String userId);

	List<MovieMultiplexSearchResultDto> searchMovies(String searchString);

	List<MovieMultiplexSearchResultDto> getAllotedMovieMultiplexByMultiplexId(String multiplexId);

	boolean deleteAllottedRecordsByMultiplexId(String multiplexId, String userId);

	boolean deleteAllottedRecordById(String id, String userId);

	MovieMultiplexDetailsDto updateMovieMultiplex(String id, MovieMultiplexDto movieMultiplexDto, String userId);

	MovieMultiplexDetailsDto getMovieMultiplexById(String id);

}
