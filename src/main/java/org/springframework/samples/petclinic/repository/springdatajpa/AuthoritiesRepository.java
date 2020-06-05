package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Authorities;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{
	
	@Query("SELECT a.authority FROM Authorities a WHERE a.username = :username")
	String findAuthority(@Param("username") String username) throws DataAccessException;
			
}
