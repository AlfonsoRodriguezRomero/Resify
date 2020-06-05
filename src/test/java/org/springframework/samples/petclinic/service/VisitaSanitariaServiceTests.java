/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file visept in compliance with the License.
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.VisitaSanitaria;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
class VisitaSanitariaServiceTests {

	@Autowired
	protected VisitaSanitariaService visitaSanitariaService;

	@Autowired
	protected ManagerService managerService;

	@Autowired
	protected AncianoService ancianoService;

	@Autowired
	protected ResidenciaService residenciaService;

	@Test
	@Transactional
	void debeEncontrarVisitaSanitariaConIdCorrecto() {
		VisitaSanitaria vis = this.visitaSanitariaService.findVisitaSanitariaById(1);
		Assertions.assertTrue(vis.getDescripcion().equals("visita sanitaria"));
		Assertions.assertTrue(vis.getMotivo().equals("Ejemplo"));
		Assertions.assertTrue(vis.getAnciano().equals(this.ancianoService.findAncianoById(22)));

	}

	// No se encuentra visita sanitaria que no existe
	@Test
	@Transactional
	void noDebeEncontrarVisitaSanitariaConIdIncorrecto() {
		VisitaSanitaria vis = this.visitaSanitariaService.findVisitaSanitariaById(111);
		Assertions.assertNull(vis);

	}

	@Test
	@Transactional
	void debeEncontrarTodasLasVisitaSanitariasPorManager() {
		Manager manager = this.managerService.findManagerById(9);
		Iterable<VisitaSanitaria> vis = this.visitaSanitariaService.findAllMine(manager);

		ArrayList<VisitaSanitaria> visitaSanitariaes = new ArrayList<VisitaSanitaria>();
		for (VisitaSanitaria e : vis) {
			visitaSanitariaes.add(e);
		}
		VisitaSanitaria visitaSanitaria1 = visitaSanitariaes.get(0);
		Assertions.assertTrue(visitaSanitaria1.getDescripcion().equals("visita sanitaria"));
	}

	// No se podra encontrar las visitas sanitarias de un manager que no existe
	@Test
	@Transactional
	void noDebeEncontrarTodasLasVisitaSanitariasParaManagerInexistente() {
		Manager manager = this.managerService.findManagerById(111);
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.visitaSanitariaService.findAllMine(manager);
		});
	}

	@Test
	@Transactional
	public void debeCrearVisitaSanitaria() {
		Manager manager = this.managerService.findManagerById(9);
		Iterable<VisitaSanitaria> vis1 = this.visitaSanitariaService.findAllMine(manager);
		ArrayList<VisitaSanitaria> visitaSanitarias1 = new ArrayList<VisitaSanitaria>();
		Residencia residencia = this.residenciaService.findResidenciaById(4);
		Anciano anciano = ancianoService.findAncianoById(22);

		for (VisitaSanitaria b : vis1) {
			visitaSanitarias1.add(b);
		}

		int total = visitaSanitarias1.size();

		VisitaSanitaria visitaSanitaria = new VisitaSanitaria();
		visitaSanitaria.setDescripcion("Prueba");
		visitaSanitaria.setFecha(java.sql.Date.valueOf(LocalDate.now().minusDays(5)));
		visitaSanitaria.setHoraInicio(LocalTime.of(9, 0));
		visitaSanitaria.setHoraFin(LocalTime.of(20, 0));
		visitaSanitaria.setMotivo("motivo prueba");
		visitaSanitaria.setSanitario("sanitario prueba");
		visitaSanitaria.setResidencia(residencia);
		visitaSanitaria.setAnciano(anciano);
		visitaSanitaria.setId(10);

		this.visitaSanitariaService.saveVisitaSanitaria(visitaSanitaria);

		Iterable<VisitaSanitaria> vis2 = this.visitaSanitariaService.findAllMine(manager);
		ArrayList<VisitaSanitaria> visitaSanitarias2 = new ArrayList<VisitaSanitaria>();
		for (VisitaSanitaria e : vis2) {
			visitaSanitarias2.add(e);
		}

		Assertions.assertTrue(visitaSanitarias2.size() == total + 1);

		Assertions.assertTrue(visitaSanitaria.getId() != null);
	}

	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoVisitaSanitariaEnBlanco() {

		VisitaSanitaria vis = new VisitaSanitaria();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.visitaSanitariaService.saveVisitaSanitaria(vis);
		});
	}

	@Test
	@Transactional
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	public void debeEliminarVisitaSanitaria() {
		Manager manager = this.managerService.findManagerById(9);
		Iterable<VisitaSanitaria> vis1 = this.visitaSanitariaService.findAllMine(manager);
		ArrayList<VisitaSanitaria> visitaSanitarias1 = new ArrayList<VisitaSanitaria>();
		for (VisitaSanitaria b : vis1) {
			visitaSanitarias1.add(b);
		}

		int total = visitaSanitarias1.size();

		VisitaSanitaria visitaSanitaria = visitaSanitarias1.get(0);
		this.visitaSanitariaService.deleteVisitaSanitaria(visitaSanitaria);

		Iterable<VisitaSanitaria> vis2 = this.visitaSanitariaService.findAllMine(manager);
		if (Iterables.size(vis2) != 0) {
			ArrayList<VisitaSanitaria> visitaSanitarias2 = new ArrayList<VisitaSanitaria>();
			for (VisitaSanitaria e : vis2) {
				visitaSanitarias2.add(e);

			}
			Assertions.assertTrue(visitaSanitarias2.size() == total - 1);
			Assertions.assertTrue(visitaSanitaria.getId() == null);

		} else {
			Assertions.assertTrue(Iterables.size(vis2) == 0);
		}

	}

	@Test
	@Transactional
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	void debeContarTodasLasVisitaSanitarias() {
		Assertions.assertTrue(this.visitaSanitariaService.countVisitasSanitarias().equals(1L));
	}

	@Test
	@Transactional
	void debeHacerMediaVisitaSanitariasPorResidencia() {
		Assertions.assertTrue(this.visitaSanitariaService.avgVisitasSanitariasByResidencia().equals(0.25));
	}

}
