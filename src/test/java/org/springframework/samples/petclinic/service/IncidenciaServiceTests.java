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
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Incidencia;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class IncidenciaServiceTests {

	@Autowired
	protected IncidenciaService incidenciaService;

	@Autowired
	protected ManagerService managerService;

	@Autowired
	protected ResidenciaService residenciaService;

	@Test
	void debeEncontrarIncidenciaConIdCorrecto() {
		Incidencia i = this.incidenciaService.findIncidenciaById(1);
		Assertions.assertTrue(i.getTitulo().equals("titulo1"));
		Assertions.assertTrue(i.getResidencia().getNombre().equals("Residencia 1"));

	}

	@Test
	void noDebeEncontrarIncidenciaConIdIncorrecto() {
		Incidencia i = this.incidenciaService.findIncidenciaById(111);
		Assertions.assertNull(i);

	}

	@Test
	void debeEncontrarTodasLasIncidencias() {
		Iterable<Incidencia> bas = this.incidenciaService.findAll();

		ArrayList<Incidencia> basc = new ArrayList<Incidencia>();
		for (Incidencia b : bas) {
			basc.add(b);
		}
		Incidencia ba = basc.get(0);
		Assertions.assertTrue(ba.getTitulo().equals("titulo1"));
	}

	@Test
	void debeEncontrarTodasLasIncidenciasPorManager() {
		Manager m = this.managerService.findManagerById(3);
		Iterable<Incidencia> bas = this.incidenciaService.findAllMine(m);

		ArrayList<Incidencia> basc = new ArrayList<Incidencia>();
		for (Incidencia b : bas) {
			basc.add(b);
		}

		Incidencia ba = basc.get(0);
		Assertions.assertTrue(ba.getTitulo().equals("titulo1"));
	}

	// No va a encontrar incidencias para un manager inexistente
	@Test
	void noDebeEncontrarTodasLasIncidenciasParaManagerInexistente() {
		Manager m = this.managerService.findManagerById(111);
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.incidenciaService.findAllMine(m);
		});
	}

	@Test
	@Transactional
	public void debeCrearIncidencia() {
		Manager m = this.managerService.findManagerById(3);
		Iterable<Incidencia> bas = this.incidenciaService.findAllMine(m);
		ArrayList<Incidencia> basc = new ArrayList<Incidencia>();
		for (Incidencia b : bas) {
			basc.add(b);
		}

		int total = basc.size();

		Residencia res = this.residenciaService.findMine(m);

		Incidencia ba = new Incidencia();
		ba.setTitulo("Prueba");
		ba.setFecha(new Date(System.currentTimeMillis() - 1));
		ba.setDescripcion("Prueba descripcion");
		ba.setResidencia(res);

		this.incidenciaService.saveIncidencia(ba);

		Iterable<Incidencia> bas2 = this.incidenciaService.findAllMine(m);
		ArrayList<Incidencia> basc2 = new ArrayList<Incidencia>();
		for (Incidencia b2 : bas2) {
			basc2.add(b2);
		}

		// Comprueba que se ha añadido a las incidencias del manager
		Assertions.assertTrue(basc2.size() == total + 1);

		// Comprueba que su id ya no es nulo
		Assertions.assertTrue(ba.getId() != null);
	}

	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoIncidenciaEnBlanco() {

		Incidencia ba = new Incidencia();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.incidenciaService.saveIncidencia(ba);
		});
	}
	
	@Test
	@Transactional
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void debeContarTodasLasIncidencias() {
		Assertions.assertTrue(this.incidenciaService.countIncidencias().equals(4L));
	}
	
	@Test
	@Transactional
	public void noDebeContarTodasLasIncidencias() {
		Assertions.assertTrue(!this.incidenciaService.countIncidencias().equals(2L));
	}



}
