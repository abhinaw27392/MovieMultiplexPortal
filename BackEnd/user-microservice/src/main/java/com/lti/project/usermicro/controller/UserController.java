package com.lti.project.usermicro.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lti.project.usermicro.dto.RegisterDto;
import com.lti.project.usermicro.dto.UserDetailDto;
import com.lti.project.usermicro.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping()
	public ResponseEntity<UserDetailDto> register(@RequestBody RegisterDto registerDto) throws Exception {
		UserDetailDto userDetailDto;
		try {
			userDetailDto = this.userService.register(registerDto);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<UserDetailDto>(userDetailDto, HttpStatus.OK);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserDetailDto>> getAllUsers() throws Exception {
		log.info("getting all users");
		List<UserDetailDto> users;
		try {
			users = this.userService.getAllUsers();
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<List<UserDetailDto>>(users, HttpStatus.OK);

	}

	@GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDetailDto> getUserDetails(@PathVariable("userId") String userId) throws Exception {
		UserDetailDto userDetailDto;
		try {
			userDetailDto = this.userService.getUserDetails(userId);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<UserDetailDto>(userDetailDto, HttpStatus.OK);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<UserDetailDto> updateUserDetails(@PathVariable("userId") String userId,
			@RequestBody UserDetailDto userDetailDto) throws Exception {
		try {
			userDetailDto = this.userService.updateUserDetails(userId, userDetailDto);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<UserDetailDto>(userDetailDto, HttpStatus.OK);
	}

	@GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDetailDto> login(Principal principal) throws Exception {
		UserDetailDto userDetail;
		try {
			userDetail = this.userService.login(principal);
		} catch (Exception e) {
			throw new WebServerException(e.getMessage(), e);
		}
		return new ResponseEntity<UserDetailDto>(userDetail, HttpStatus.OK);
	}

}
