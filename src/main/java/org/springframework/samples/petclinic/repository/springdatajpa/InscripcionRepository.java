
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Inscripcion;

public interface InscripcionRepository extends CrudRepository<Inscripcion, String> {

	Inscripcion findById(int id) throws DataAccessException;

	@Query("SELECT inscripcion FROM Inscripcion inscripcion WHERE inscripcion.anciano.id =:id")
	Iterable<Inscripcion> findAllMineAnciano(@Param("id") int id) throws DataAccessException;

	@Query("SELECT inscripcion FROM Inscripcion inscripcion WHERE inscripcion.residencia.manager.id =:id AND inscripcion.estado = 'pendiente'")
	Iterable<Inscripcion> findAllMineManager(@Param("id") int id) throws DataAccessException;

	@Query("SELECT COUNT(inscripcion) FROM Inscripcion inscripcion WHERE inscripcion.residencia.id =:id AND inscripcion.estado = 'aceptada'")
	Integer cuentaAceptadasEnResidencia(Integer id) throws DataAccessException;

	@Query("SELECT COUNT(inscripcion) FROM Inscripcion inscripcion WHERE inscripcion.estado = 'aceptada'")
	Long countInscripcionesAceptadas() throws DataAccessException;

	@Query("SELECT COUNT(inscripcion) FROM Inscripcion inscripcion WHERE inscripcion.estado = 'rechazada'")
	Long countInscripcionesRechazadas() throws DataAccessException;

}
