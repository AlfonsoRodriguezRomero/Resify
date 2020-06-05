
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Residencia;

public interface ResidenciaRepository extends CrudRepository<Residencia, String> {

	Residencia findById(int id) throws DataAccessException;

	@Query("SELECT residencia FROM Residencia residencia WHERE residencia.manager.id =:id")
	Residencia findMine(@Param("id") int id) throws DataAccessException;

	@Query("SELECT count(buenaAccion.id) FROM BuenaAccion buenaAccion WHERE buenaAccion.residencia.id =:id")
	Double countBuenasAccionesByResidenciaId(@Param("id") int id) throws DataAccessException;

	@Query("SELECT count(incidencia.id) FROM Incidencia incidencia WHERE incidencia.residencia.id =:id")
	Double countIncidenciasByResidenciaId(@Param("id") int id) throws DataAccessException;
	
	@Query("SELECT i.residencia FROM Inscripcion i WHERE i.anciano.id = :id AND i.estado = 'aceptada'")
	Residencia findResidenciaByAncianoId(@Param("id") int id) throws DataAccessException;

	@Query("SELECT r, CASE WHEN (COUNT(DISTINCT b.id) = 0) THEN 0 WHEN (COUNT(DISTINCT i.id) = 0) THEN COUNT(DISTINCT b.id) ELSE (COUNT(DISTINCT b.id)/COUNT(DISTINCT i.id)) END AS ratioAcciones FROM Residencia r, BuenaAccion b, Incidencia i WHERE r.id = i.residencia.id AND r.id = b.residencia.id GROUP BY r.id ORDER BY ratioAcciones desc")
	Iterable<Object[]> findTop() throws DataAccessException;

	@Query("SELECT r from Residencia r where r.id not in (SELECT distinct(re.id) FROM Residencia re, PeticionExcursion p, Excursion e where " + "p.estado = 'aceptada' and re.id = p.residencia.id and p.excursion.id = " + "e.id and e.organizador.id =:id)")
	Iterable<Residencia> findResidenciasSinParticipar(@Param("id") int id) throws DataAccessException;

	@Query("SELECT count(r.id) from Residencia r where r.aforo <= (SELECT count(i.id) FROM Inscripcion i where i.residencia.id = r.id AND i.estado = 'aceptada')")
	Long countResidenciasCompletas()throws DataAccessException;

}
