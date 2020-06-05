package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.PeticionExcursion;

public interface PeticionExcursionRepository extends CrudRepository<PeticionExcursion, String> {

	PeticionExcursion findById(int id) throws DataAccessException;
	
	@Query("SELECT peticion FROM PeticionExcursion peticion WHERE peticion.residencia.id =:id AND peticion.excursion.fechaInicio > :today")
	Iterable<PeticionExcursion> findByResidencia(@Param("id") int id, @Param("today") Date today) throws DataAccessException;

	@Query("SELECT peticion FROM PeticionExcursion peticion WHERE peticion.excursion.organizador.id =:id AND peticion.estado = 'pendiente'")
	Iterable<PeticionExcursion> findByOrganizador(@Param("id") int id) throws DataAccessException;
	
	@Query("SELECT count(peticion.id) FROM PeticionExcursion peticion WHERE peticion.excursion.id =:id AND peticion.estado = 'aceptada'")
	Double findByExcursionAceptada(@Param("id") int id) throws DataAccessException;

	@Query("SELECT count(peticiones.id) FROM PeticionExcursion peticiones WHERE peticiones.excursion LIKE :excursion AND peticiones.residencia.manager LIKE :manager ")
	Integer countPeticionesByExcursion(@Param("excursion") Excursion excursion, @Param("manager") Manager manager)
			throws DataAccessException;
	
	@Query("SELECT count(peticiones.id) FROM PeticionExcursion peticiones WHERE peticiones.excursion LIKE :excursion AND peticiones.residencia.manager LIKE :manager AND peticiones.estado = 'aceptada'")
	Integer countPeticionesAceptadasByExcursion(@Param("excursion") Excursion excursion, @Param("manager") Manager manager)
			throws DataAccessException;

	@Query("SELECT count(p) FROM PeticionExcursion p GROUP BY p.excursion")
	Iterable<Long> countPeticionesExcursionByExcursion() throws DataAccessException;
	
	@Query("SELECT count(p) FROM PeticionExcursion p WHERE p.estado = 'aceptada'")
	Long countPeticionesExcursionAceptadas() throws DataAccessException;

	@Query("SELECT count(p) FROM PeticionExcursion p WHERE p.estado = 'rechazada'")
	Long countPeticionesExcursionRechazadas() throws DataAccessException;

	
}