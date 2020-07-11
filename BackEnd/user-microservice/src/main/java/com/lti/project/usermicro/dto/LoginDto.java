package com.lti.project.usermicro.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class LoginDto {
	private String userName;
	@Length(min = 6)
	private String password;
}
