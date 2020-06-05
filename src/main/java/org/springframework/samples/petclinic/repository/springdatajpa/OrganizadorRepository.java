package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Organizador;


public interface OrganizadorRepository extends CrudRepository<Organizador, String>{
	
	Organizador findById(int id) throws DataAccessException;
	
//	@Query("SELECT DISTINCT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName%")
//	public Collection<Owner> findByLastName(@Param("lastName") String lastName);
	
	@Query("SELECT organizador FROM Organizador organizador WHERE organizador.user.username = :username")
	Organizador findByUsername(@Param("username") String username) throws DataAccessException;
}
