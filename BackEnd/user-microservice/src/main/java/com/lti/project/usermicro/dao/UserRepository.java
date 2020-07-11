package com.lti.project.usermicro.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lti.project.usermicro.document.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
	Optional<User> findByUsername(String username);

//	List<User> findByUsernameAndPassword(String userName, String password);
}
