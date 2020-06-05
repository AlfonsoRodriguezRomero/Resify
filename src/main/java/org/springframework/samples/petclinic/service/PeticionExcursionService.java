package org.springframework.samples.petclinic.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.PeticionExcursionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PeticionExcursionService {

	@Autowired
	private PeticionExcursionRepository peticionExcursionRepository;
	
	@Autowired
	private ExcursionService excursionService;

	@Transactional
	public PeticionExcursion findPeticionExcursionById(int id) throws DataAccessException {
		return peticionExcursionRepository.findById(id);
	}

	@Transactional
	public Iterable<PeticionExcursion> findAllMineResidencia(Residencia residencia) throws DataAccessException {
		return peticionExcursionRepository.findByResidencia(residencia.getId(), java.sql.Date.valueOf(LocalDate.now()));
	}

	@Transactional
	public Iterable<PeticionExcursion> findAllMineOrganizador(Organizador organizador) throws DataAccessException {
		return peticionExcursionRepository.findByOrganizador(organizador.getId());
	}

	@Transactional
	public Double countPeticionExcursionAceptadaByExcursion(Excursion excursion) throws DataAccessException {
		return peticionExcursionRepository.findByExcursionAceptada(excursion.getId());
	}

	@Transactional
	public void save(PeticionExcursion peticionExcursion) throws DataAccessException {
		peticionExcursionRepository.save(peticionExcursion);
	}

	@Transactional(readOnly = true)
	public int countPeticionesByExcursion(Excursion excursion, Manager manager) throws DataAccessException {
		return peticionExcursionRepository.countPeticionesByExcursion(excursion, manager);
	}
	
	@Transactional(readOnly = true)
	public int countPeticionesByExcursionAceptada(Excursion excursion, Manager manager) throws DataAccessException {
		return peticionExcursionRepository.countPeticionesAceptadasByExcursion(excursion, manager);
	}

	@Transactional
	public Long countPeticionesExcursion() {
		return peticionExcursionRepository.count();
	}

	@Transactional
	public Double avgPeticionesExcursionByExcursion() {
		Double res = 0.;
		if (!this.excursionService.countExcursiones().equals(0L)) {
			res = Double.valueOf(this.peticionExcursionRepository.count())/this.excursionService.countExcursiones().doubleValue();
		}
		return res;
	}

	@Transactional
	public Long countPeticionesExcursionAceptadas() {
		return peticionExcursionRepository.countPeticionesExcursionAceptadas();
	}

	@Transactional
	public Double ratioPeticionesExcursionAceptadas() {
		Double res = 0.;
		if (!countPeticionesExcursion().equals(0L)) {
			res = countPeticionesExcursionAceptadas().doubleValue()/countPeticionesExcursion().doubleValue();
		}
		return res;
	}

	@Transactional
	public Long countPeticionesExcursionRechazadas() {
		return peticionExcursionRepository.countPeticionesExcursionRechazadas();
	}

	@Transactional
	public Double ratioPeticionesExcursionRechazadas() {
		Double res = 0.;
		if (!countPeticionesExcursion().equals(0L)) {
			res = countPeticionesExcursionRechazadas().doubleValue()/countPeticionesExcursion().doubleValue();
		}
		return res;
	}

}