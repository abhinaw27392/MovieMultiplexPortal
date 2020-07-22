package com.lti.project.movie.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lti.project.movie.dao.MovieMultiplexRepository;
import com.lti.project.movie.dao.MovieRepository;
import com.lti.project.movie.document.Movie;
import com.lti.project.movie.document.MovieMultiplex;
import com.lti.project.movie.dto.MovieDto;
import com.lti.project.movie.dto.MovieMultiplexDetailsDto;
import com.lti.project.movie.dto.MovieMultiplexDto;
import com.lti.project.movie.dto.MovieMultiplexSearchResultDto;
import com.lti.project.movie.dto.MultiplexDto;
import com.lti.project.movie.dto.UserDetailDto;
import com.lti.project.movie.exception.CustomException;
import com.lti.project.movie.feign.MultiplexFeignProxy;
import com.lti.project.movie.feign.UserDetailFeignProxy;

@Service
public class MovieServiceImpl implements MovieService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private MovieRepository repository;
	private MovieMultiplexRepository movieMultiplexRepository;
	private MultiplexFeignProxy multiplexFeignProxy;
	private UserDetailFeignProxy userDetailFeignProxy;

	public MovieServiceImpl(MovieRepository repository, MovieMultiplexRepository movieMultiplexRepository,
			MultiplexFeignProxy multiplexFeignProxy, UserDetailFeignProxy userDetailFeignProxy) {
		this.repository = repository;
		this.movieMultiplexRepository = movieMultiplexRepository;
		this.multiplexFeignProxy = multiplexFeignProxy;
		this.userDetailFeignProxy = userDetailFeignProxy;
	}

	@Override
	public MovieDto addMovie(MovieDto movieDto, String userId) throws CustomException {

		try {
			validateUser(userId);
			if (movieDto.getMovieName() == null || movieDto.getMovieName().isEmpty())
				throw new Exception("movie name can not be null or empty");

			// converting moviDto to Movie document
			Movie movie = new Movie(null, movieDto.getMovieName(), movieDto.getMovieCategory(),
					movieDto.getMovieProducer(), movieDto.getMovieDirector(), movieDto.getReleaseDate());

			// saving movie to movie_micro database
			movie = repository.save(movie);

			// again converting Movie document to movieDto
			movieDto = new MovieDto(movie.getId(), movie.getMovieName(), movie.getMovieCategory(),
					movie.getMovieProducer(), movie.getMovieDirector(), movie.getReleaseDate());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return movieDto;
	}

	@Override
	public List<MovieDto> getAllMovies() throws CustomException {
		ArrayList<MovieDto> movieList;
		try {
			Iterator<Movie> movies = repository.findAll().iterator();
			movieList = new ArrayList<MovieDto>();
			while (movies.hasNext()) {
				Movie movie = movies.next();
				MovieDto movieDto = new MovieDto(movie.getId(), movie.getMovieName(), movie.getMovieCategory(),
						movie.getMovieProducer(), movie.getMovieDirector(), movie.getReleaseDate());
				movieList.add(movieDto);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return movieList;
	}

	@Override
	public MovieDto getMovieById(String movieId) throws CustomException {
		MovieDto movieDto;
		try {
			Optional<Movie> movieInDb = repository.findById(movieId);
			if (!movieInDb.isPresent())
				throw new Exception("Error during fetching movie details: " + movieId);

			Movie movie = movieInDb.get();
			movieDto = new MovieDto(movie.getId(), movie.getMovieName(), movie.getMovieCategory(),
					movie.getMovieProducer(), movie.getMovieDirector(), movie.getReleaseDate());

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return movieDto;
	}

	@Override
	public MovieDto updateMovie(String movieId, MovieDto movieDto, String userId) throws CustomException {
		try {
			validateUser(userId);
			Optional<Movie> movieInDb = repository.findById(movieId);
			if (!movieInDb.isPresent())
				throw new Exception("Error while updating movie details, movieId: " + movieId);

			if (movieDto.getMovieName() == null || movieDto.getMovieName().isEmpty())
				throw new Exception("movie name can not be null or empty");

			Movie movie = movieInDb.get();
			movie.setMovieName(movieDto.getMovieName());
			movie.setMovieCategory(movieDto.getMovieCategory());
			movie.setMovieDirector(movieDto.getMovieDirector());
			movie.setMovieProducer(movieDto.getMovieProducer());
			movie.setReleaseDate(movieDto.getReleaseDate());

			movie = repository.save(movie);

			movieDto = new MovieDto(movie.getId(), movie.getMovieName(), movie.getMovieCategory(),
					movie.getMovieProducer(), movie.getMovieDirector(), movie.getReleaseDate());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return movieDto;
	}

	@Override
	public boolean deleteMovieById(String movieId, String userId) throws CustomException {
		try {
			validateUser(userId);
			Optional<Movie> movieInDb = repository.findById(movieId);
			if (movieInDb.isPresent()) {
				repository.deleteById(movieId);
				movieMultiplexRepository.deleteAllByMovieId(movieId);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public MovieMultiplexDetailsDto addMovieToMultiplex(String userId, MovieMultiplexDto movieMultiplexDto)
			throws CustomException {
		MovieMultiplexDetailsDto movieMultiplexDetailsDto;
		try {
			validateUser(userId);
			validateScreenNumber(movieMultiplexDto);

			// convert movieMultiplexDto to MovieMultiplex document
			if (movieMultiplexDto.getMovieId() != null && movieMultiplexDto.getMultiplexId() != null) {
				MovieMultiplex movieMultiplex = new MovieMultiplex(null, movieMultiplexDto.getMovieId(),
						movieMultiplexDto.getMultiplexId(), movieMultiplexDto.getScreenName(), userId);

				movieMultiplex = movieMultiplexRepository.save(movieMultiplex);

				movieMultiplexDetailsDto = getMovieMultiplexDetailsDto(movieMultiplex);
				return movieMultiplexDetailsDto;
			} else {
				throw new CustomException("movie Name or Multiplex Name can not be null");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public List<MovieMultiplexDetailsDto> getAllAllotedMovieMultiplex() throws CustomException {

		List<MovieMultiplexDetailsDto> movieAndMultiplexList = new ArrayList<MovieMultiplexDetailsDto>();
		try {
			Iterator<MovieMultiplex> moviesAndMultiplexes = this.movieMultiplexRepository.findAll().iterator();
			while (moviesAndMultiplexes.hasNext()) {
				MovieMultiplex movieMultiplex = moviesAndMultiplexes.next();
				MovieMultiplexDetailsDto movieMultiplexDetailsDto = getMovieMultiplexDetailsDto(movieMultiplex);
				movieAndMultiplexList.add(movieMultiplexDetailsDto);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return movieAndMultiplexList;
	}

	@Override
	public List<MovieMultiplexDetailsDto> getAllotedMovieMultiplexByUserId(String userId) throws CustomException {

		List<MovieMultiplexDetailsDto> movieAndMultiplexList = new ArrayList<MovieMultiplexDetailsDto>();
		try {
			validateUser(userId);
			List<MovieMultiplex> moviesAndMultiplexes = this.movieMultiplexRepository.findByUserId(userId);
			for (MovieMultiplex movieMultiplex : moviesAndMultiplexes) {
				MovieMultiplexDetailsDto movieMultiplexDetailsDto = getMovieMultiplexDetailsDto(movieMultiplex);
				movieAndMultiplexList.add(movieMultiplexDetailsDto);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return movieAndMultiplexList;
	}

	private MovieMultiplexDetailsDto getMovieMultiplexDetailsDto(MovieMultiplex movieMultiplex) {

		// getting movieDto by movieId saved to MovieMultiplex collection
		MovieDto movieDto = this.getMovieById(movieMultiplex.getMovieId());

		// calling multiplex-microservice api through feignClient proxy interface
		MultiplexDto multiplexDto = this.multiplexFeignProxy.getMultiplexById(movieMultiplex.getMultiplexId())
				.getBody();

		// calling user-microservice api through feignClient proxy interface
		UserDetailDto userDetailDto = this.userDetailFeignProxy.getUserDetails(movieMultiplex.getUserId()).getBody();

		// convert movieMultiplex document to movieMultiplexDetailDto
		return new MovieMultiplexDetailsDto(movieMultiplex.getId(), movieDto, multiplexDto,
				movieMultiplex.getScreenName(), userDetailDto);
	}

	private void validateUser(String userId) throws Exception {
		// validation for user
		UserDetailDto userDetailDto = this.userDetailFeignProxy.getUserDetails(userId).getBody();
		if (!userDetailDto.getRole().equals("ADMIN"))
			throw new Exception("To Perform this operation you need ADMIN privilege");
	}

	@Override
	public List<MovieMultiplexSearchResultDto> searchMovies(String searchString) throws CustomException {
		List<MovieMultiplexSearchResultDto> movieMultiplexList = new ArrayList<MovieMultiplexSearchResultDto>();
		try {
			List<MovieDto> movies = getMoviesBySearchString(searchString);

			for (MovieDto movie : movies) {
				List<MovieMultiplex> movieMultiplexListInDb = movieMultiplexRepository.findAllByMovieId(movie.getId());
				for (MovieMultiplex movieMultiplex : movieMultiplexListInDb) {
					MultiplexDto multiplex = this.multiplexFeignProxy.getMultiplexById(movieMultiplex.getMultiplexId())
							.getBody();
					movieMultiplexList.add(new MovieMultiplexSearchResultDto(movie.getMovieName(),
							multiplex.getMultiplexName(), multiplex.getAddress(), movieMultiplex.getScreenName()));
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return movieMultiplexList;
	}

	private List<MovieDto> getMoviesBySearchString(String searchString) throws Exception {
		if (searchString == null || searchString.isEmpty())
			throw new Exception("search string can not be null or empty");

		List<Movie> movies = repository.findByMovieNameIgnoreCaseStartsWith(searchString);
		List<MovieDto> movieList = new ArrayList<MovieDto>();

		for (Movie movie : movies) {
			movieList.add(new MovieDto(movie.getId(), movie.getMovieName(), movie.getMovieCategory(),
					movie.getMovieProducer(), movie.getMovieDirector(), movie.getReleaseDate()));
		}
		return movieList;
	}

	@Override
	public List<MovieMultiplexSearchResultDto> getAllotedMovieMultiplexByMultiplexId(String multiplexId)
			throws CustomException {
		List<MovieMultiplexSearchResultDto> movieMultiplexList = new ArrayList<MovieMultiplexSearchResultDto>();
		try {
			List<MovieMultiplex> movieMultiplexes = movieMultiplexRepository.findAllByMultiplexId(multiplexId);
			for (MovieMultiplex movieMultiplex : movieMultiplexes) {
				MovieDto movie = this.getMovieById(movieMultiplex.getMovieId());
				MultiplexDto multiplex = this.multiplexFeignProxy.getMultiplexById(movieMultiplex.getMultiplexId())
						.getBody();
				movieMultiplexList.add(new MovieMultiplexSearchResultDto(movie.getMovieName(),
						multiplex.getMultiplexName(), multiplex.getAddress(), movieMultiplex.getScreenName()));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return movieMultiplexList;
	}

	@Override
	public boolean deleteAllottedRecordsByMultiplexId(String multiplexId, String userId) throws CustomException {
		try {
			validateUser(userId);
			List<MovieMultiplex> movieMultiplexList = movieMultiplexRepository.findAllByMultiplexId(multiplexId);
			if (movieMultiplexList.size() > 0) {
				movieMultiplexRepository.deleteAllByMultiplexId(multiplexId);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public boolean deleteAllottedRecordById(String id, String userId) throws CustomException {
		try {
			validateUser(userId);
			Optional<MovieMultiplex> recordInDb = movieMultiplexRepository.findById(id);
			if (recordInDb.isPresent()) {
				movieMultiplexRepository.deleteById(id);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public MovieMultiplexDetailsDto updateMovieMultiplex(String id, MovieMultiplexDto movieMultiplexDto, String userId)
			throws CustomException {

		MovieMultiplexDetailsDto movieMultiplexDetailsDto;
		try {
			validateUser(userId);

			// validation for screen allotment when updating
			List<MovieMultiplex> movieMultiplexList = movieMultiplexRepository.findAllByMultiplexIdAndScreenName(
					movieMultiplexDto.getMultiplexId(), movieMultiplexDto.getScreenName());

			Optional<MovieMultiplex> currentDataInDb = movieMultiplexRepository.findById(id);
			MovieMultiplex currentData = currentDataInDb.get();

			for (MovieMultiplex movieMultiplexInDb : movieMultiplexList) {
				MultiplexDto multiplex = this.multiplexFeignProxy.getMultiplexById(movieMultiplexInDb.getMultiplexId())
						.getBody();
				MovieDto movie = this.getMovieById(movieMultiplexInDb.getMovieId());
				if (movieMultiplexInDb.getMultiplexId().equals(movieMultiplexDto.getMultiplexId())
						&& movieMultiplexInDb.getScreenName().equals(movieMultiplexDto.getScreenName())) {

					// checking with current record
					if (!currentData.getMovieId().equals(movieMultiplexInDb.getMovieId())
							|| !currentData.getMultiplexId().equals(movieMultiplexInDb.getMultiplexId())
							|| !currentData.getScreenName().equals(movieMultiplexInDb.getScreenName())) {
						throw new Exception(movie.getMovieName().toUpperCase() + " is already alloted to "
								+ movieMultiplexDto.getScreenName() + " of "
								+ multiplex.getMultiplexName().toUpperCase() + ", " + multiplex.getAddress());
					}
				}
			}
			// convert movieMultiplexDto to MovieMultiplex document
			if (movieMultiplexDto.getMovieId() != null && movieMultiplexDto.getMultiplexId() != null) {
				MovieMultiplex movieMultiplex = new MovieMultiplex(id, movieMultiplexDto.getMovieId(),
						movieMultiplexDto.getMultiplexId(), movieMultiplexDto.getScreenName(), userId);
				movieMultiplex = movieMultiplexRepository.save(movieMultiplex);

				movieMultiplexDetailsDto = getMovieMultiplexDetailsDto(movieMultiplex);
				return movieMultiplexDetailsDto;
			} else {
				throw new CustomException("movie Name or Multiplex Name can not be null");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
	}

	private void validateScreenNumber(MovieMultiplexDto movieMultiplexDto) throws Exception {

		// validation for screen allotment when adding
		List<MovieMultiplex> movieMultiplexList = movieMultiplexRepository.findAllByMultiplexIdAndScreenName(
				movieMultiplexDto.getMultiplexId(), movieMultiplexDto.getScreenName());

		for (MovieMultiplex movieMultiplexInDb : movieMultiplexList) {
			MultiplexDto multiplex = this.multiplexFeignProxy.getMultiplexById(movieMultiplexInDb.getMultiplexId())
					.getBody();
			MovieDto movie = this.getMovieById(movieMultiplexInDb.getMovieId());
			if (movieMultiplexInDb.getMultiplexId().equals(movieMultiplexDto.getMultiplexId())
					&& movieMultiplexInDb.getScreenName().equals(movieMultiplexDto.getScreenName())) {
				throw new Exception(movie.getMovieName().toUpperCase() + " is already alloted to "
						+ movieMultiplexDto.getScreenName() + " of " + multiplex.getMultiplexName().toUpperCase() + ", "
						+ multiplex.getAddress());
			}
		}
	}

	@Override
	public MovieMultiplexDetailsDto getMovieMultiplexById(String id) {
		MovieMultiplexDetailsDto movieMultiplexDetailsDto;
		try {
			Optional<MovieMultiplex> movieMultiplexInDb = movieMultiplexRepository.findById(id);
			if (!movieMultiplexInDb.isPresent())
				throw new Exception("Error during fetching movie multiplex details: " + id);

			MovieMultiplex movieMultiplex = movieMultiplexInDb.get();
			movieMultiplexDetailsDto = getMovieMultiplexDetailsDto(movieMultiplex);

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(e.getMessage());
		}
		return movieMultiplexDetailsDto;
	}

}
