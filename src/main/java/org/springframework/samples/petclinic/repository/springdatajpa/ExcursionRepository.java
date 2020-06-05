
package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Excursion;

public interface ExcursionRepository extends CrudRepository<Excursion, String> {

	Excursion findById(int id) throws DataAccessException;

	@Query("SELECT excursion FROM Excursion excursion WHERE excursion.organizador.id =:id")
	Iterable<Excursion> findAllMine(@Param("id") int id) throws DataAccessException;

	@Query("SELECT excursion FROM Excursion excursion WHERE excursion.finalMode = true AND excursion.fechaInicio > :today")
	Iterable<Excursion> findAllPublishedAndFuture(@Param("today") Date today) throws DataAccessException;

	@Query("SELECT e FROM Residencia r, Anciano a, Inscripcion i, PeticionExcursion p, Excursion e WHERE i.anciano.id =:id AND e.fechaInicio > :today "
			+ "AND r.id = i.residencia.id AND i.estado = 'aceptada' and i.anciano.id = a.id AND p.residencia.id = r.id AND "
			+ "p.excursion.id = e.id AND p.estado = 'aceptada' AND e.finalMode = true")
	Iterable<Excursion> findAllMineAnciano(@Param("id") int id, @Param("today") Date today);

	@Query("SELECT e FROM PeticionExcursion p JOIN p.excursion e WHERE p.residencia.id =:id AND p.estado = 'aceptada' AND e.finalMode = true AND e.fechaFin <= :today")
	Iterable<Excursion> findAllFinishedResidencia(@Param("id") int id, @Param("today") Date today);
}
