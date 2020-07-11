package com.lti.project.movie.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieMultiplex {
	@Id
	private String id;
	private String movieId;
	private String multiplexId;
	private String screenName;
	private String userId;
}
