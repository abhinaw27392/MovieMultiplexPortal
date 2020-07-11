package com.lti.project.multiplex.feign;

import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lti.project.multiplex.dto.MovieDto;
import com.lti.project.multiplex.dto.MovieMultiplexSearchResultDto;

@FeignClient(name = "movie-microservice")
@RibbonClient(name = "movie-microservice")
public interface MovieFeignProxy {

	@GetMapping(value = "/api/movie", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieDto>> getAllMovies();

	@GetMapping(value = "/api/movie/searchMultiplex/{multiplexId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieMultiplexSearchResultDto>> getAllotedMovieMultiplexByMultiplexId(
			@PathVariable("multiplexId") String multiplexId);
}
