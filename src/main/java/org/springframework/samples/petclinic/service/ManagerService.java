package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.ManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagerService {

	private ManagerRepository managerRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public ManagerService(final ManagerRepository managerRepository) {
		this.managerRepository = managerRepository;
	}

	@Transactional(readOnly = true)
	public Manager findManagerById(final int id) throws DataAccessException {
		return this.managerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Iterable<Manager> findManagers() throws DataAccessException {
		return this.managerRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Manager findManagerByUserName(String username) throws DataAccessException {
		return managerRepository.findByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public Manager findManagerByUsername(final String username) throws DataAccessException {
		return this.managerRepository.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public Residencia findResidenciaByManagerUsername(String username) throws DataAccessException {
		return managerRepository.findResidenciaByManagerUsername(username);
	}

	@Transactional
	public void saveManager(final Manager manager) throws DataAccessException {
		// creating manager
		this.managerRepository.save(manager);
		// creating user
		this.userService.saveUser(manager.getUser());
		// creating authorities
		this.authoritiesService.saveAuthorities(manager.getUser().getUsername(), "manager");
	}

	@Transactional
	public Long countManagers() {
		return this.managerRepository.count();
	}

}
