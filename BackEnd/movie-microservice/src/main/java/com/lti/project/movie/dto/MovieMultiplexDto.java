package com.lti.project.movie.dto;

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
public class MovieMultiplexDto {
	private String id;
	private String movieId;
	private String multiplexId;
	private String screenName;
}
