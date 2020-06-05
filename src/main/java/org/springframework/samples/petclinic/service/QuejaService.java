package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Queja;
import org.springframework.samples.petclinic.repository.springdatajpa.QuejaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuejaService {

	@Autowired
	private QuejaRepository quejaRepository;	
	
	@Autowired
	private AncianoService ancianoService;	
	
	@Autowired
	private ResidenciaService residenciaService;	

	@Transactional(readOnly = true)
	public Iterable<Queja> findQuejasByManager(Manager manager) throws DataAccessException {
		return quejaRepository.findQuejasByManagerId(manager.getId());
	}

	@Transactional(readOnly = true)
	public Queja findQuejaById(int id) throws DataAccessException {
		return quejaRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Double countQuejasHoyByAnciano(Anciano anciano) throws DataAccessException {
		
		return quejaRepository.countQuejasByTiempoYAncianoId(anciano.getId(),Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	@Transactional
	public void saveQueja(Queja queja) throws DataAccessException {
		quejaRepository.save(queja);	
	}

	@Transactional
	public Long countQuejas() {
		return this.quejaRepository.count();
	}

	@Transactional
	public Double avgQuejasByAnciano() {
		Double res = 0.;
		if (!this.ancianoService.countAncianos().equals(0L)) {
			res = Double.valueOf(this.quejaRepository.count())/this.ancianoService.countAncianos().doubleValue();
		}
		return res;
	}		


	@Transactional
	public Double avgQuejasByResidencia() {
		Double res = 0.;
		if (!this.residenciaService.countResidencias().equals(0L)) {
			res = Double.valueOf(this.quejaRepository.count())/this.residenciaService.countResidencias().doubleValue();
		}
		return res;
	}		
	

}
