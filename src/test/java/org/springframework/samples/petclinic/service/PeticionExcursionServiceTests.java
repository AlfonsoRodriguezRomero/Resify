/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PeticionExcursionServiceTests {

	@Autowired
	protected PeticionExcursionService	peticionExcursionService;

	@Autowired
	protected ManagerService		managerService;

	@Autowired
	protected ResidenciaService		residenciaService;
	
	@Autowired
	protected OrganizadorService		organizadorService;
	
	@Autowired
	protected ExcursionService excursionService;

	@Test
	void debeEncontrarTodasLasPeticionExcursionPorResidencia() {
		Residencia residencia = this.residenciaService.findResidenciaById(2);
		Iterable<PeticionExcursion> peticiones = this.peticionExcursionService.findAllMineResidencia(residencia);

		ArrayList<PeticionExcursion> peticionesE = new ArrayList<PeticionExcursion>();
		for (PeticionExcursion p : peticiones) {
			peticionesE.add(p);
		}

		PeticionExcursion p = this.peticionExcursionService.findPeticionExcursionById(2);
		PeticionExcursion pe = peticionesE.get(0);
		Assertions.assertTrue(pe.equals(p));
	}
	
	@Test
	void debeEncontrarTodasLasPeticionExcursionPorOrganizador() {
		Organizador organizador = this.organizadorService.findOrganizadorById(1);
		Iterable<PeticionExcursion> peticiones = this.peticionExcursionService.findAllMineOrganizador(organizador);

		ArrayList<PeticionExcursion> peticionesE = new ArrayList<PeticionExcursion>();
		for (PeticionExcursion p : peticiones) {
			peticionesE.add(p);
		}

		PeticionExcursion p = this.peticionExcursionService.findPeticionExcursionById(3);
		PeticionExcursion pe = peticionesE.get(0);
		Assertions.assertTrue(pe.equals(p));
	}
	
	@Test
	@Transactional
	public void debeCrearPeticionExcursionYGenerarId() {
		Manager manager = this.managerService.findManagerById(4);
		Residencia residencia = this.residenciaService.findMine(manager);
		Excursion excursion = this.excursionService.findExcursionById(1);
		Integer pe = this.peticionExcursionService.countPeticionesByExcursion(excursion, manager);
		
		PeticionExcursion petex = new PeticionExcursion();
		petex.setDeclaracion("Declaracion de prueba");
		petex.setEstado("pendiente");
		petex.setFecha(new Date(System.currentTimeMillis() - 1));
		petex.setExcursion(excursion);
		petex.setResidencia(residencia);
		
		this.peticionExcursionService.save(petex);
		
		int newPe = this.peticionExcursionService.countPeticionesByExcursion(excursion, manager);

		//Comprueba que se ha añadido la peticion de excursion
		Assertions.assertTrue(pe + newPe == 1);

		//Comprueba que su id ya no es nulo
		Assertions.assertTrue(petex.getId() != null);
	}

	@Test
	@Transactional
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	void debeContarTodasLasPeticionExcursionAceptadasPorExcursion() {
		Excursion excursion = this.excursionService.findExcursionById(3);
		Double npeticiones = this.peticionExcursionService.countPeticionExcursionAceptadaByExcursion(excursion);

		Assertions.assertTrue(npeticiones.equals(2.));
	}
	
	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoPeticionExcursionEnBlanco() {

		PeticionExcursion pe = new PeticionExcursion();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.peticionExcursionService.save(pe);
		});
	}
//	
//	@Test
//	@Transactional
//	public void debeLanzarExcepcionCreandoPeticionExcursionExcursionAforoMaximo() {
//		Manager manager = this.managerService.findManagerById(4);
//		Residencia residencia = this.residenciaService.findAllMine(manager).iterator().next();
//		Excursion excursion = this.excursionService.findExcursionById(1);
//		excursion.setNumeroResidencias(1);
//		
//		PeticionExcursion pe = new PeticionExcursion();
//		pe.setDeclaracion("Declaracion de prueba");
//		pe.setEstado("pendiente");
//		pe.setFecha(new Date(System.currentTimeMillis() - 1));
//		pe.setExcursion(excursion);
//		pe.setResidencia(residencia);
//
//		Assertions.assertThrows(ConstraintViolationException.class, () -> {
//			this.peticionExcursionService.save(pe);
//		});
//	}
	
	@Test
	@Transactional
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void debeContarTodasLasPeticiones() {
		Assertions.assertTrue(this.peticionExcursionService.countPeticionesExcursion().equals(6L));
	}
	
	@Test
	@Transactional
	public void noDebeContarTodasLasPeticiones() {
		Assertions.assertTrue(!this.peticionExcursionService.countPeticionesExcursion().equals(4L));
	}
	
	@Test
	@Transactional
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void debeHacerMediaPeticionesPorExcursion() {
		Assertions.assertTrue(this.peticionExcursionService.avgPeticionesExcursionByExcursion().equals(0.6));
	}
	
	@Test
	@Transactional
	public void noDebeHacerMediaPeticionesPorExcursion() {
		Assertions.assertTrue(!this.peticionExcursionService.avgPeticionesExcursionByExcursion().equals(0.2));
	}
	
	@Test
	@Transactional
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void debeContarTodasLasPeticionesAceptadas() {
		Assertions.assertTrue(this.peticionExcursionService.countPeticionesExcursionAceptadas().equals(5L));
	}
	
	@Test
	@Transactional
	public void noDebeContarTodasLasPeticionesAceptadas() {
		Assertions.assertTrue(!this.peticionExcursionService.countPeticionesExcursionAceptadas().equals(4L));
	}
	
	@Test
	@Transactional
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void debeHacerRatioPeticionesAceptadas() {
		Assertions.assertTrue(this.peticionExcursionService.ratioPeticionesExcursionAceptadas().equals(2.5/3));
	}
	
	@Test
	@Transactional
	public void noDebeHacerRatioPeticionesAceptadas() {
		Assertions.assertTrue(!this.peticionExcursionService.ratioPeticionesExcursionAceptadas().equals(0.5));
	}
	
	@Test
	@Transactional
	public void debeContarTodasLasPeticionesRechazadas() {
		Assertions.assertTrue(this.peticionExcursionService.countPeticionesExcursionRechazadas().equals(0L));
	}
	
	@Test
	@Transactional
	public void noDebeContarTodasLasPeticionesRechazadas() {
		Assertions.assertTrue(!this.peticionExcursionService.countPeticionesExcursionRechazadas().equals(4L));
	}
	
	@Test
	@Transactional
	public void debeHacerRatioPeticionesRechazadas() {
		Assertions.assertTrue(this.peticionExcursionService.ratioPeticionesExcursionRechazadas().equals(0.0));
	}
	
	@Test
	@Transactional
	public void noDebeHacerRatioPeticionesRechazadas() {
		Assertions.assertTrue(!this.peticionExcursionService.ratioPeticionesExcursionRechazadas().equals(0.5));
	}
	
	

}
