package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.ExcursionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcursionService {

	@Autowired
	private ExcursionRepository excursionRepository;	
	
	@Autowired
	private OrganizadorService organizadorService;
	
	@Transactional(readOnly = true)
	public Excursion findExcursionById(int id) throws DataAccessException {
		return excursionRepository.findById(id);
	}

	@Transactional
	public void saveExcursion(Excursion excursion) throws DataAccessException {
		excursionRepository.save(excursion);	
	}
	
	@Transactional
	public void deleteExcursion(Excursion excursion) throws DataAccessException {
		excursionRepository.delete(excursion);	
	}

	@Transactional
	public Iterable<Excursion> findAllMine(Organizador organizador) {
		return excursionRepository.findAllMine(organizador.getId());
	}	
	
	@Transactional
	public Iterable<Excursion> findAllPublished() {
		return excursionRepository.findAllPublishedAndFuture(java.sql.Date.valueOf(LocalDate.now()));
	}
	
	@Transactional
	public Iterable<Excursion> findAllGone(Residencia resi) {
		return excursionRepository.findAllFinishedResidencia(resi.getId(), java.sql.Date.valueOf(LocalDate.now()));
	}
	
	@Transactional
	public Iterable<Excursion> findAll() {
		return excursionRepository.findAll();
	}

	public Iterable<Excursion> findAllMineAnciano(Anciano anciano) {
		return excursionRepository.findAllMineAnciano(anciano.getId(), java.sql.Date.valueOf(LocalDate.now()));
	}

	@Transactional
	public Long countExcursiones() {
		return excursionRepository.count();
	}
	
	@Transactional
	public Double avgExcursionesByOrganizador() {
		Double res = 0.;
		if (!this.organizadorService.countOrganizadores().equals(0L)) {
			res = Double.valueOf(this.excursionRepository.count())/this.organizadorService.countOrganizadores().doubleValue();
		}
		return res;
	}		

}
