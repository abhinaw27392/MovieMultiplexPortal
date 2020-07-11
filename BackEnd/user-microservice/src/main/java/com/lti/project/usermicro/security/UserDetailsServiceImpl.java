package com.lti.project.usermicro.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lti.project.usermicro.dao.UserRepository;
import com.lti.project.usermicro.document.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			return new UserPrincipal(user.get());
		} else {
			throw new UsernameNotFoundException(username + " is not an authorized user!");
		}
	}

}
