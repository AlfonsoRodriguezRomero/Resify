package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.springdatajpa.ActividadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActividadService {

	@Autowired
	private ActividadRepository actividadRepository;	
	
	@Autowired
	private ResidenciaService residenciaService;	
	
//	@Autowired
//	private UserService userService;
//	
//	@Autowired
//	private AuthoritiesService authoritiesService;

//	@Autowired
//	public ActividadService(ActividadRepository actividadRepository) {
//		this.actividadRepository = actividadRepository;
//	}	

	@Transactional(readOnly = true)
	public Actividad findActividadById(int id) throws DataAccessException {
		return actividadRepository.findById(id);
	}

	@Transactional
	public void saveActividad(Actividad actividad) throws DataAccessException {
		actividadRepository.save(actividad);	
	}
	
	@Transactional
	public void deleteActividad(Actividad actividad) throws DataAccessException {
		actividadRepository.delete(actividad);	
	}
	
	@Transactional
	public Iterable<Actividad> findAllMine(Manager manager) {
		return actividadRepository.findAllMine(manager.getId());
	}
	
	@Transactional
	public Iterable<Actividad> findAll() {
		return actividadRepository.findAll();
	}

	public Iterable<Actividad> findAllMineAnciano(Anciano anciano) {
		return actividadRepository.findAllMineAnciano(anciano.getId(), java.sql.Date.valueOf(LocalDate.now()));
	}
	
	@Transactional
	public Long countActividades() {
		return actividadRepository.count();
	}

	@Transactional
	public Double avgActividadesByResidencia() {
		Double res = 0.;
		if (!this.residenciaService.countResidencias().equals(0L)) {
			res = Double.valueOf(this.actividadRepository.count())/this.residenciaService.countResidencias().doubleValue();
		}
		return res;
	}


}
