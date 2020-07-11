package com.lti.project.multiplex.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lti.project.multiplex.document.Multiplex;

@Repository
public interface MultiplexRepository extends CrudRepository<Multiplex, String> {

	List<Multiplex> findByMultiplexNameIgnoreCaseStartsWith(String searchString);

}
