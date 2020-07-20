package com.lti.project.multiplex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lti.project.multiplex.dao.MultiplexRepository;
import com.lti.project.multiplex.document.Multiplex;
import com.lti.project.multiplex.dto.MovieMultiplexSearchResultDto;
import com.lti.project.multiplex.dto.MultiplexDto;
import com.lti.project.multiplex.dto.UserDetailDto;
import com.lti.project.multiplex.exception.CustomException;
import com.lti.project.multiplex.feign.MovieFeignProxy;
import com.lti.project.multiplex.feign.UserDetailFeignProxy;

@Service
public class MultiplexServiceImpl implements MultiplexService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private MultiplexRepository repository;
	private UserDetailFeignProxy userDetailFeignProxy;
	private MovieFeignProxy movieFeignProxy;

	public MultiplexServiceImpl(MultiplexRepository repository, UserDetailFeignProxy userDetailFeignProxy,
			MovieFeignProxy movieFeignProxy) {
		this.repository = repository;
		this.userDetailFeignProxy = userDetailFeignProxy;
		this.movieFeignProxy = movieFeignProxy;
	}

	@Override
	public MultiplexDto addMultiplex(MultiplexDto multiplexDto, String userId) throws CustomException {

		try {
			validateUser(userId);
			// validation for adding multiplex
			if (multiplexDto.getMultiplexName() == null || multiplexDto.getMultiplexName().isEmpty())
				throw new Exception("multiplex name can not be null or empty");

			// converting multiplexDto to multiplex document
			Multiplex multiplex = new Multiplex(null, multiplexDto.getMultiplexName(), multiplexDto.getAddress(),
					multiplexDto.getNumberOfScreens());

			// saving multiplex to multiplex_micro database
			multiplex = repository.save(multiplex);

			// again converting multiplex document to multiplexDto
			multiplexDto = new MultiplexDto(multiplex.getId(), multiplex.getMultiplexName(), multiplex.getAddress(),
					multiplex.getNumberOfScreens());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return multiplexDto;
	}

	@Override
	public List<MultiplexDto> getAllMultiplexs() throws CustomException {
		ArrayList<MultiplexDto> multiplexList;
		try {
			Iterator<Multiplex> multiplexs = repository.findAll().iterator();
			multiplexList = new ArrayList<MultiplexDto>();
			while (multiplexs.hasNext()) {
				Multiplex multiplex = multiplexs.next();
				MultiplexDto multiplexDto = new MultiplexDto(multiplex.getId(), multiplex.getMultiplexName(),
						multiplex.getAddress(), multiplex.getNumberOfScreens());
				multiplexList.add(multiplexDto);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return multiplexList;
	}

	@Override
	public MultiplexDto getMultiplexById(String multiplexId) throws CustomException {
		MultiplexDto multiplexDto;
		try {
			Optional<Multiplex> multiplexInDb = repository.findById(multiplexId);
			if (!multiplexInDb.isPresent())
				throw new Exception("Error during fetching multiplex details" + multiplexId);
			Multiplex multiplex = multiplexInDb.get();
			multiplexDto = new MultiplexDto(multiplex.getId(), multiplex.getMultiplexName(), multiplex.getAddress(),
					multiplex.getNumberOfScreens());

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}

		return multiplexDto;
	}

	@Override
	public MultiplexDto updateMultiplex(String multiplexId, MultiplexDto multiplexDto, String userId)
			throws CustomException {
		try {
			validateUser(userId);
			Optional<Multiplex> multiplexInDb = repository.findById(multiplexId);
			if (!multiplexInDb.isPresent())
				throw new Exception("Error while updating multiplex details, multiplexId: " + multiplexId);

			if (multiplexDto.getMultiplexName() == null || multiplexDto.getMultiplexName().isEmpty())
				throw new Exception("multiplex name can not be null or empty");

			Multiplex multiplex = multiplexInDb.get();
			multiplex.setMultiplexName(multiplexDto.getMultiplexName());
			multiplex.setAddress(multiplexDto.getAddress());
			multiplex.setNumberOfScreens(multiplexDto.getNumberOfScreens());

			multiplex = repository.save(multiplex);

			multiplexDto = new MultiplexDto(multiplex.getId(), multiplex.getMultiplexName(), multiplex.getAddress(),
					multiplex.getNumberOfScreens());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return multiplexDto;
	}

	@Override
	public boolean deleteMultiplexById(String multiplexId, String userId) throws CustomException {
		try {
			validateUser(userId);
			Optional<Multiplex> multiplexInDb = repository.findById(multiplexId);
			if (multiplexInDb.isPresent()) {
				repository.deleteById(multiplexId);
				this.movieFeignProxy.deleteAllottedRecordByMultiplexId(multiplexId, userId);
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
	}

	private void validateUser(String userId) throws Exception {
		// validation for user
		UserDetailDto userDetailDto = this.userDetailFeignProxy.getUserDetails(userId).getBody();
		if (!userDetailDto.getRole().equals("ADMIN"))
			throw new Exception("To Perform this operation you need ADMIN privilege");
	}

	@Override
	public List<MovieMultiplexSearchResultDto> searchMultiplexes(String searchString) throws CustomException {
		List<MovieMultiplexSearchResultDto> multiplexMovieList = new ArrayList<MovieMultiplexSearchResultDto>();
		try {
			List<MultiplexDto> multiplexes = getMultiplexBySearchString(searchString);

			for (MultiplexDto multiplex : multiplexes) {
				List<MovieMultiplexSearchResultDto> movieMultiplexListInDb = this.movieFeignProxy
						.getAllotedMovieMultiplexByMultiplexId(multiplex.getId()).getBody();
				for (MovieMultiplexSearchResultDto movieMultiplex : movieMultiplexListInDb) {
					multiplexMovieList.add(movieMultiplex);
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return multiplexMovieList;
	}

	private List<MultiplexDto> getMultiplexBySearchString(String searchString) throws Exception {

		if (searchString == null || searchString.isEmpty())
			throw new Exception("search string can not be null or empty");

		List<Multiplex> multiplexes = repository.findByMultiplexNameIgnoreCaseStartsWith(searchString);
		List<MultiplexDto> multiplexList = new ArrayList<MultiplexDto>();

		for (Multiplex multiplex : multiplexes) {
			multiplexList.add(new MultiplexDto(multiplex.getId(), multiplex.getMultiplexName(), multiplex.getAddress(),
					multiplex.getNumberOfScreens()));
		}
		return multiplexList;

	}

}
