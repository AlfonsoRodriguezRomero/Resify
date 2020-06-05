package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.repository.springdatajpa.OrganizadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizadorService {

	private OrganizadorRepository organizadorRepository;	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public OrganizadorService(OrganizadorRepository organizadorRepository) {
		this.organizadorRepository = organizadorRepository;
	}	

	@Transactional(readOnly = true)
	public Organizador findOrganizadorById(int id) throws DataAccessException {
		return organizadorRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Iterable<Organizador> findOrganizadores() throws DataAccessException {
		return organizadorRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Organizador findOrganizadorByUsername(String username) throws DataAccessException {
		return organizadorRepository.findByUsername(username);
	}

//	@Transactional
//	public Organizador findOrganizadorAutentificado(Principal p) {
//		return organizadorRepository.findByUsername(p.getName());
//	}
	
	@Transactional
	public void saveOrganizador(Organizador organizador) throws DataAccessException {
		//creating organizador
		organizadorRepository.save(organizador);		
		//creating user
		userService.saveUser(organizador.getUser());
		//creating authorities
		authoritiesService.saveAuthorities(organizador.getUser().getUsername(), "organizador");
	}
	
	@Transactional
	public Long countOrganizadores() {
		return this.organizadorRepository.count();
	}		

}
