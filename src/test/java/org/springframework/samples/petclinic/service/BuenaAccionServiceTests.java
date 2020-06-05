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
import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class BuenaAccionServiceTests {

	@Autowired
	protected BuenaAccionService	buenaAccionService;

	@Autowired
	protected ManagerService		managerService;

	@Autowired
	protected ResidenciaService		residenciaService;


	@Test
	void debeEncontrarBuenaAccionConIdCorrecto() {
		BuenaAccion ba = this.buenaAccionService.findBuenaAccionById(1);
		Assertions.assertTrue(ba.getTitulo().equals("titulo1"));
		Assertions.assertTrue(ba.getResidencia().getNombre().equals("Residencia 1"));

	}

	@Test
	void debeEncontrarTodasLasBuenasAcciones() {
		Iterable<BuenaAccion> bas = this.buenaAccionService.findAll();

		ArrayList<BuenaAccion> basc = new ArrayList<BuenaAccion>();
		for (BuenaAccion b : bas) {
			basc.add(b);
		}
		BuenaAccion ba = basc.get(0);
		Assertions.assertTrue(ba.getTitulo().equals("titulo1"));
	}

	@Test
	void debeEncontrarTodasLasBuenasAccionesPorManager() {
		Manager m = this.managerService.findManagerById(3);
		Iterable<BuenaAccion> bas = this.buenaAccionService.findAllMine(m);

		ArrayList<BuenaAccion> basc = new ArrayList<BuenaAccion>();
		for (BuenaAccion b : bas) {
			basc.add(b);
		}

		BuenaAccion ba = basc.get(0);
		Assertions.assertTrue(ba.getTitulo().equals("titulo1"));
	}

	@Test
	@Transactional
	public void debeCrearBuenaAccion() {
		Manager m = this.managerService.findManagerById(3);
		Iterable<BuenaAccion> bas = this.buenaAccionService.findAllMine(m);
		ArrayList<BuenaAccion> basc = new ArrayList<BuenaAccion>();
		for (BuenaAccion b : bas) {
			basc.add(b);
		}

		int total = basc.size();

		Residencia res = this.residenciaService.findMine(m);

		BuenaAccion ba = new BuenaAccion();
		ba.setTitulo("Prueba");
		ba.setFecha(new Date(System.currentTimeMillis() - 1));
		ba.setDescripcion("Prueba descripcion");
		ba.setResidencia(res);

		this.buenaAccionService.saveBuenaAccion(ba);

		Iterable<BuenaAccion> bas2 = this.buenaAccionService.findAllMine(m);
		ArrayList<BuenaAccion> basc2 = new ArrayList<BuenaAccion>();
		for (BuenaAccion b2 : bas2) {
			basc2.add(b2);
		}

		//Comprueba que se ha añadido a las buenas acciones del manager
		Assertions.assertTrue(basc2.size() == total + 1);

		//Comprueba que su id ya no es nulo
		Assertions.assertTrue(ba.getId() != null);
	}

	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoBuenaAccionEnBlanco() {

		BuenaAccion ba = new BuenaAccion();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.buenaAccionService.saveBuenaAccion(ba);
		});
	}
	
	@Test
	@Transactional
	public void debeContarTodasLasBuenasAcciones() {
		Assertions.assertTrue(this.buenaAccionService.countBuenasAcciones().equals(4L));
	}
	
	@Test
	@Transactional
	public void noDebeContarTodasLasBuenasAcciones() {
		Assertions.assertTrue(!this.buenaAccionService.countBuenasAcciones().equals(2L));
	}



}
