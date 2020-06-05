package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Feedback;
import org.springframework.samples.petclinic.model.Manager;

public interface FeedbackRepository extends CrudRepository<Feedback, String> {

	Feedback findById(int id) throws DataAccessException;
	
	@Query("SELECT f FROM Feedback f WHERE f.residencia.id =:id AND f.excursion.fechaInicio > :today")
	Iterable<Feedback> findByResidencia(@Param("id") int id, @Param("today") Date today) throws DataAccessException;

	@Query("SELECT f FROM Feedback f WHERE f.excursion.organizador.id =:id and f.descartaFeedback = false")
	Iterable<Feedback> findByOrganizador(@Param("id") int id) throws DataAccessException;
	
	@Query("SELECT count(f.id) FROM Feedback f WHERE f.excursion.id =:id")
	Double findByExcursionAceptada(@Param("id") int id) throws DataAccessException;

	@Query("SELECT count(f.id) FROM Feedback f WHERE f.excursion LIKE :excursion AND f.residencia.manager LIKE :manager ")
	Integer countFeedbacksByExcursion(@Param("excursion") Excursion excursion, @Param("manager") Manager manager)
			throws DataAccessException;

	@Query("SELECT count(f) FROM Feedback f GROUP BY f.excursion")
	Iterable<Long> countFeedbacksByExcursion() throws DataAccessException;
}