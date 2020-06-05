
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Anciano;

public interface AncianoRepository extends CrudRepository<Anciano, String> {

	Anciano findById(int id) throws DataAccessException;

	@Query("SELECT anciano FROM Anciano anciano WHERE anciano.user.username = :username")
	Anciano findByUsername(@Param("username") String username) throws DataAccessException;

	@Query("SELECT a FROM Anciano a where a.id in (select i.anciano.id from Inscripcion i where i.estado = 'aceptada' "
			+ "and i.residencia.id =:id)")
	Iterable<Anciano> findAncianosMiResidencia(@Param("id") int id) throws DataAccessException;

	@Query("SELECT count(a.id) FROM Anciano a where a.id in (select i.anciano.id from Inscripcion i where i.estado = 'aceptada' "
			+ "and i.residencia.id =:id)")
	Integer countAncianosByResidenciaId(@Param("id") int id) throws DataAccessException;

	@Query("SELECT count(i) FROM Inscripcion i WHERE i.estado = 'aceptada'")
	Long countAncianosInResidencia() throws DataAccessException;
}
