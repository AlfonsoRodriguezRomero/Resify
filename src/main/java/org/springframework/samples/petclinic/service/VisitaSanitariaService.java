package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.VisitaSanitaria;
import org.springframework.samples.petclinic.repository.springdatajpa.VisitaSanitariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitaSanitariaService {

	@Autowired
	private VisitaSanitariaRepository visitaSanitariaRepository;	
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Transactional(readOnly = true)
	public VisitaSanitaria findVisitaSanitariaById(int id) throws DataAccessException {
		return visitaSanitariaRepository.findById(id);
	}

	@Transactional
	public void saveVisitaSanitaria(VisitaSanitaria visitaSanitaria) throws DataAccessException {
		visitaSanitariaRepository.save(visitaSanitaria);	
	}
	
	@Transactional
	public void deleteVisitaSanitaria(VisitaSanitaria visitaSanitaria) throws DataAccessException {
		visitaSanitariaRepository.delete(visitaSanitaria);	
	}

	@Transactional
	public Iterable<VisitaSanitaria> findAllMine(final Manager manager) {
		Residencia residencia = this.residenciaService.findMine(manager);
		int id = residencia.getId();
		return this.visitaSanitariaRepository.findAllMine(id);
	}

	@Transactional
	public Long countVisitasSanitarias() {
		return this.visitaSanitariaRepository.count();
	}

	@Transactional
	public Double avgVisitasSanitariasByResidencia() {
		Double res = 0.;
		if (!this.residenciaService.countResidencias().equals(0L)) {
			res = Double.valueOf(this.visitaSanitariaRepository.count())/this.residenciaService.countResidencias().doubleValue();
		}
		return res;
	}		

}
