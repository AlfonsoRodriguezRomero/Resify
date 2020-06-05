package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.repository.springdatajpa.AncianoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AncianoService {

	private AncianoRepository	ancianoRepository;

	@Autowired
	private UserService			userService;

	@Autowired
	private AuthoritiesService	authoritiesService;
	
	@Autowired
	private ResidenciaService	residenciaService;


	@Autowired
	public AncianoService(final AncianoRepository ancianoRepository) {
		this.ancianoRepository = ancianoRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Anciano> findAncianos() throws DataAccessException {
		return this.ancianoRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Anciano findAncianoById(final int id) throws DataAccessException {
		return this.ancianoRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Anciano findAncianoByUsername(final String username) throws DataAccessException {
		return this.ancianoRepository.findByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public Iterable<Anciano> findAncianosMiResidencia(final Residencia residencia) throws DataAccessException {
		return this.ancianoRepository.findAncianosMiResidencia(residencia.getId());
	}
	
	@Transactional(readOnly = true)
	public Integer countAncianosMiResidencia(final Residencia residencia) throws DataAccessException {
		return this.ancianoRepository.countAncianosByResidenciaId(residencia.getId());
	}

	@Transactional
	public void saveAnciano(final Anciano anciano) throws DataAccessException {
		//creating anciano
		this.ancianoRepository.save(anciano);
		//creating user
		this.userService.saveUser(anciano.getUser());
		//creating authorities
		this.authoritiesService.saveAuthorities(anciano.getUser().getUsername(), "anciano");
	}

	@Transactional
	public Long countAncianos() {
		return this.ancianoRepository.count();
	}

	@Transactional
	public Double avgAncianosByResidencia() {
		Double res = 0.;
		if (!this.residenciaService.countResidencias().equals(0L)) {
			res = this.ancianoRepository.countAncianosInResidencia().doubleValue()/this.residenciaService.countResidencias().doubleValue();
		}
		return res;
	}

}
