package com.lti.project.movie.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieDto {
	private String id;
	private String movieName;
	private String movieCategory;
	private String movieProducer;
	private String movieDirector;
	private LocalDate releaseDate;
}
