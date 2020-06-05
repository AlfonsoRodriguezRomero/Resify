package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.repository.springdatajpa.BuenaAccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuenaAccionService {

	@Autowired
	private BuenaAccionRepository buenaAccionRepository;	


	@Transactional(readOnly = true)
	public BuenaAccion findBuenaAccionById(int id) throws DataAccessException {
		return buenaAccionRepository.findById(id);
	}

	@Transactional
	public void saveBuenaAccion(BuenaAccion buenaAccion) throws DataAccessException {
		buenaAccionRepository.save(buenaAccion);	
	}
	
	@Transactional
	public Iterable<BuenaAccion> findAllMine(Manager manager) {
		return buenaAccionRepository.findAllMine(manager.getId());
	}
	
	@Transactional
	public Iterable<BuenaAccion> findAll() {
		return buenaAccionRepository.findAll();
	}

	@Transactional
	public Long countBuenasAcciones() {
		return buenaAccionRepository.count();
	}		
	
}
