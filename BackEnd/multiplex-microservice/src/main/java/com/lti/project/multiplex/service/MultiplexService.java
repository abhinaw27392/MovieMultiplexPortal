package com.lti.project.multiplex.service;

import java.util.List;

import com.lti.project.multiplex.dto.MovieMultiplexSearchResultDto;
import com.lti.project.multiplex.dto.MultiplexDto;

public interface MultiplexService {

	List<MultiplexDto> getAllMultiplexs();

	MultiplexDto addMultiplex(MultiplexDto multiplexDto, String userId);

	MultiplexDto getMultiplexById(String multiplexId);

	MultiplexDto updateMultiplex(String multiplexId, MultiplexDto multiplexDto, String userId);

	boolean deleteMultiplexById(String multiplexId, String userId);

	List<MovieMultiplexSearchResultDto> searchMultiplexes(String searchString);

}
