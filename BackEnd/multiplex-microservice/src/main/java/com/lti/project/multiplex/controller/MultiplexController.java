package com.lti.project.multiplex.controller;

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

import com.lti.project.multiplex.dto.MovieMultiplexSearchResultDto;
import com.lti.project.multiplex.dto.MultiplexDto;
import com.lti.project.multiplex.service.MultiplexService;

@RestController
@RequestMapping("/api/multiplex")
public class MultiplexController {

	private MultiplexService multiplexService;

	public MultiplexController(MultiplexService multiplexService) {
		this.multiplexService = multiplexService;
	}

	@PostMapping()
	public ResponseEntity<MultiplexDto> addMultiplex(@RequestBody MultiplexDto multiplexDto,
			@RequestParam String userId) throws Exception {
		try {
			multiplexDto = this.multiplexService.addMultiplex(multiplexDto, userId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<MultiplexDto>(multiplexDto, HttpStatus.OK);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MultiplexDto>> getAllMultiplexs() throws Exception {
		List<MultiplexDto> multiplexList;
		try {
			multiplexList = this.multiplexService.getAllMultiplexs();
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<List<MultiplexDto>>(multiplexList, HttpStatus.OK);
	}

	@GetMapping(value = "/{multiplexId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MultiplexDto> getMultiplexById(@PathVariable("multiplexId") String multiplexId)
			throws Exception {
		MultiplexDto multiplexDto;
		try {
			multiplexDto = this.multiplexService.getMultiplexById(multiplexId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<MultiplexDto>(multiplexDto, HttpStatus.OK);
	}

	@PutMapping("/{multiplexId}")
	public ResponseEntity<MultiplexDto> updateMultiplex(@PathVariable String multiplexId,
			@RequestBody MultiplexDto multiplexDto, @RequestParam String userId) throws Exception {
		try {
			multiplexDto = this.multiplexService.updateMultiplex(multiplexId, multiplexDto, userId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<MultiplexDto>(multiplexDto, HttpStatus.OK);
	}

	@DeleteMapping("/{multiplexId}")
	public ResponseEntity<Map<String, Boolean>> deleteMultiplexById(@PathVariable String multiplexId,
			@RequestParam String userId) throws Exception {
		try {
			boolean isDeleted = this.multiplexService.deleteMultiplexById(multiplexId, userId);
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

	@GetMapping(value = "/searchMultiplex/{searchString}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MovieMultiplexSearchResultDto>> searchMultiplexes(@PathVariable String searchString)
			throws Exception {
		List<MovieMultiplexSearchResultDto> multiplexList;
		try {
			multiplexList = this.multiplexService.searchMultiplexes(searchString);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<List<MovieMultiplexSearchResultDto>>(multiplexList, HttpStatus.OK);
	}

}
