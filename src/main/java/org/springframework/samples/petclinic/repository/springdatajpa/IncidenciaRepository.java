package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Incidencia;


public interface IncidenciaRepository extends  CrudRepository<Incidencia, String>{
	
	Incidencia findById(int id) throws DataAccessException;
	
	@Query("SELECT a FROM Incidencia a WHERE a.residencia.manager.id =:id")
	Iterable<Incidencia> findAllMine(@Param("id") int id) throws DataAccessException;
	
}
