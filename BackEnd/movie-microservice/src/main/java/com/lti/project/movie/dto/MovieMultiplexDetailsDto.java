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
public class MovieMultiplexDetailsDto {
	private String id;
	private MovieDto movie;
	private MultiplexDto multiplex;
	private String screenName;
	private UserDetailDto userDetail;
}
