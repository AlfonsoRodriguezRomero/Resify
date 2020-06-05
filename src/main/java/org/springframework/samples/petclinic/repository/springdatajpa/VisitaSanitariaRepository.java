
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.VisitaSanitaria;

public interface VisitaSanitariaRepository extends CrudRepository<VisitaSanitaria, String> {

	VisitaSanitaria findById(int id) throws DataAccessException;

	@Query("SELECT visita FROM VisitaSanitaria visita WHERE visita.residencia.id =:id")
	Iterable<VisitaSanitaria> findAllMine(@Param("id") int id) throws DataAccessException;
	
}
