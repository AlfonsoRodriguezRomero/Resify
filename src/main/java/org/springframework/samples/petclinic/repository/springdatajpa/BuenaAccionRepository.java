package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.BuenaAccion;


public interface BuenaAccionRepository extends  CrudRepository<BuenaAccion, String>{
	
	BuenaAccion findById(int id) throws DataAccessException;
	
	@Query("SELECT a FROM BuenaAccion a WHERE a.residencia.manager.id =:id")
	Iterable<BuenaAccion> findAllMine(@Param("id") int id) throws DataAccessException;
	
}
