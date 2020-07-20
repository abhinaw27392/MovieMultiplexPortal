package com.lti.project.multiplex.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@DeleteMapping(value = "/api/movie/movieMultiplex/delete/{multiplexId}")
	public ResponseEntity<Map<String, Boolean>> deleteAllottedRecordByMultiplexId(@PathVariable String multiplexId, @RequestParam String userId);
}
