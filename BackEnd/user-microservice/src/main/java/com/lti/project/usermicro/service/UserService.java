package com.lti.project.usermicro.service;

import java.security.Principal;
import java.util.List;

import com.lti.project.usermicro.dto.RegisterDto;
import com.lti.project.usermicro.dto.UserDetailDto;

public interface UserService {
	public UserDetailDto register(RegisterDto registerDto);

	public List<UserDetailDto> getAllUsers();

	public UserDetailDto getUserDetails(String userId);

	public UserDetailDto updateUserDetails(String userId, UserDetailDto userDetailDto);

	public UserDetailDto login(Principal principal);
}
