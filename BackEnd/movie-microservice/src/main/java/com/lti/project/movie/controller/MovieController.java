package com.lti.project.movie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lti.project.movie.dto.MovieDto;
import com.lti.project.movie.dto.MovieMultiplexDetailsDto;
import com.lti.project.movie.dto.MovieMultiplexDto;
import com.lti.project.movie.dto.MovieMultiplexSearchResultDto;
import com.lti.project.movie.service.MovieService;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

	private MovieService movieService;

	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@PostMapping()
	public ResponseEntity<MovieDto> addMovie(@RequestBody MovieDto movieDto, @RequestParam String userId)
			throws Exception {
		try {
			movieDto = this.movieService.addMovie(movieDto, userId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<MovieDto>(movieDto, HttpStatus.OK);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieDto>> getAllMovies() throws Exception {
		List<MovieDto> movieList;
		try {
			movieList = this.movieService.getAllMovies();
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<List<MovieDto>>(movieList, HttpStatus.OK);
	}

	@GetMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MovieDto> getMovieById(@PathVariable String movieId) throws Exception {
		MovieDto movieDto;
		try {
			movieDto = this.movieService.getMovieById(movieId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<MovieDto>(movieDto, HttpStatus.OK);
	}

	@PutMapping("/{movieId}")
	public ResponseEntity<MovieDto> updateMovie(@PathVariable String movieId, @RequestBody MovieDto movieDto,
			@RequestParam String userId) throws Exception {
		try {
			movieDto = this.movieService.updateMovie(movieId, movieDto, userId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<MovieDto>(movieDto, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Boolean>> deleteMovieById(@PathVariable String movieId,
			@RequestParam String userId) throws Exception {
		try {
			boolean isDeleted = this.movieService.deleteMovieById(movieId, userId);
			Map<String, Boolean> map = new HashMap<>();
			if (isDeleted) {
				map.put("deleted", true);
				return new ResponseEntity<Map<String, Boolean>>(map, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return null;
	}

	@PostMapping("/movieMultiplex")
	public ResponseEntity<MovieMultiplexDetailsDto> addMovieToMultiplex(@RequestParam String userId,
			@RequestBody MovieMultiplexDto movieMultiplexDto) throws Exception {
		MovieMultiplexDetailsDto movieMultiplexDetailsDto;
		try {
			movieMultiplexDetailsDto = this.movieService.addMovieToMultiplex(userId, movieMultiplexDto);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<MovieMultiplexDetailsDto>(movieMultiplexDetailsDto, HttpStatus.OK);
	}

	@PutMapping("/movieMultiplex/{id}")
	public ResponseEntity<MovieMultiplexDetailsDto> updateMovieToMultiplex(@PathVariable String id,
			@RequestBody MovieMultiplexDto movieMultiplexDto, @RequestParam String userId) throws Exception {
		MovieMultiplexDetailsDto movieMultiplexDetailsDto;
		try {
			movieMultiplexDetailsDto = this.movieService.updateMovieMultiplex(id, movieMultiplexDto, userId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<MovieMultiplexDetailsDto>(movieMultiplexDetailsDto, HttpStatus.OK);
	}

	@GetMapping(value = "/movieMultiplex/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MovieMultiplexDetailsDto> getMovieMultiplexById(@PathVariable String id) throws Exception {
		MovieMultiplexDetailsDto movieMultiplexDetailsDto;
		try {
			movieMultiplexDetailsDto = this.movieService.getMovieMultiplexById(id);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<MovieMultiplexDetailsDto>(movieMultiplexDetailsDto, HttpStatus.OK);
	}

	@GetMapping(value = "/movieMultiplex", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieMultiplexDetailsDto>> getAllAllotedMovieMultiplex() throws Exception {
		List<MovieMultiplexDetailsDto> movieMultiplexDetailsDtoList;
		try {
			movieMultiplexDetailsDtoList = this.movieService.getAllAllotedMovieMultiplex();
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<List<MovieMultiplexDetailsDto>>(movieMultiplexDetailsDtoList, HttpStatus.OK);
	}

	@GetMapping(value = "/movieMultiplex/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieMultiplexDetailsDto>> getAllotedMovieMultiplexByUserId(@PathVariable String userId)
			throws Exception {
		List<MovieMultiplexDetailsDto> movieMultiplexDetailsDtoList;
		try {
			movieMultiplexDetailsDtoList = this.movieService.getAllotedMovieMultiplexByUserId(userId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<List<MovieMultiplexDetailsDto>>(movieMultiplexDetailsDtoList, HttpStatus.OK);
	}

	@DeleteMapping(value = "/movieMultiplex/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteAllottedRecordById(@PathVariable String id,
			@RequestParam String userId) throws Exception {
		try {
			boolean isDeleted = this.movieService.deleteAllottedRecordById(id, userId);
			Map<String, Boolean> map = new HashMap<>();
			if (isDeleted) {
				map.put("deleted", true);
				return new ResponseEntity<Map<String, Boolean>>(map, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return null;
	}

	@DeleteMapping(value = "/movieMultiplex/delete/{multiplexId}")
	public ResponseEntity<Map<String, Boolean>> deleteAllottedRecordByMultiplexId(@PathVariable String multiplexId,
			@RequestParam String userId) throws Exception {
		try {
			boolean isDeleted = this.movieService.deleteAllottedRecordsByMultiplexId(multiplexId, userId);
			Map<String, Boolean> map = new HashMap<>();
			if (isDeleted) {
				map.put("deleted", true);
				return new ResponseEntity<Map<String, Boolean>>(map, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return null;
	}

	@GetMapping(value = "/searchMultiplex/{multiplexId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieMultiplexSearchResultDto>> getAllotedMovieMultiplexByMultiplexId(
			@PathVariable("multiplexId") String multiplexId) throws Exception {

		List<MovieMultiplexSearchResultDto> movieMultiplexList;
		try {
			movieMultiplexList = this.movieService.getAllotedMovieMultiplexByMultiplexId(multiplexId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<List<MovieMultiplexSearchResultDto>>(movieMultiplexList, HttpStatus.OK);

	}

	@GetMapping(value = "/searchMovie/{searchString}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieMultiplexSearchResultDto>> searchMovies(@PathVariable String searchString)
			throws Exception {
		List<MovieMultiplexSearchResultDto> movieMultiplexDetailsList;
		try {
			movieMultiplexDetailsList = this.movieService.searchMovies(searchString);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<List<MovieMultiplexSearchResultDto>>(movieMultiplexDetailsList, HttpStatus.OK);
	}

}
