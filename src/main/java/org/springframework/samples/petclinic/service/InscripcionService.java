package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Inscripcion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InscripcionService {

	@Autowired
	private InscripcionRepository inscripcionRepository;

	//tested
	@Transactional(readOnly = true)
	public Inscripcion findInscripcionById(final int id) throws DataAccessException {
		return this.inscripcionRepository.findById(id);
	}
	//tested
	@Transactional
	public void saveInscripcion(final Inscripcion inscripcion) throws DataAccessException {
		this.inscripcionRepository.save(inscripcion);
	}
	//tested
	@Transactional
	public Iterable<Inscripcion> findAllMineAnciano(final Anciano anciano) {
		return this.inscripcionRepository.findAllMineAnciano(anciano.getId());
	}
	//tested
	@Transactional
	public Iterable<Inscripcion> findAllMineManager(final Manager manager) {
		return this.inscripcionRepository.findAllMineManager(manager.getId());
	}
	//tested
	@Transactional
	public Integer cuentaAceptadasEnResidencia(final Residencia residencia) {
		return this.inscripcionRepository.cuentaAceptadasEnResidencia(residencia.getId());
	}
	//tested
	@Transactional
	public Iterable<Inscripcion> findAll() {
		return this.inscripcionRepository.findAll();
	}
	//tested
	@Transactional
	public Long countInscripciones() {
		return this.inscripcionRepository.count();
	}
	//tested
	@Transactional
	public Long countInscripcionesAceptadas() {
		return this.inscripcionRepository.countInscripcionesAceptadas();
	}
	//tested
	@Transactional
	public Double ratioInscripcionesAceptadas() {
		Double res = 0.;
		if (!this.countInscripciones().equals(0L)) {
			res = this.countInscripcionesAceptadas().doubleValue()/this.countInscripciones().doubleValue();
		}
		return res;
	}
	//tested
	@Transactional
	public Long countInscripcionesRechazadas() {
		return this.inscripcionRepository.countInscripcionesRechazadas();
	}
	//tested
	@Transactional
	public Double ratioInscripcionesRechazadas() {
		Double res = 0.;
		if (!this.countInscripciones().equals(0L)) {
			res = this.countInscripcionesRechazadas().doubleValue()/this.countInscripciones().doubleValue();
		}
		return res;
	}

}
