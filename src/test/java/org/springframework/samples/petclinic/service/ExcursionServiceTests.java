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
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ExcursionServiceTests {

	@Autowired
	protected ExcursionService		excursionService;

	@Autowired
	protected OrganizadorService	organizadorService;

	@Autowired
	protected AncianoService		ancianoService;


	@Test
	void debeEncontrarExcursionConIdCorrecto() {
		Excursion exc = this.excursionService.findExcursionById(1);
		Assertions.assertTrue(exc.getRatioAceptacion().equals(2.0));
		Assertions.assertTrue(exc.getNumeroResidencias() == 4);
		Assertions.assertTrue(exc.isFinalMode());
		Assertions.assertTrue(exc.getOrganizador().equals(this.organizadorService.findOrganizadorById(1)));

	}

	@Test
	void debeEncontrarTodasLasExcursiones() {
		Iterable<Excursion> exc = this.excursionService.findAll();

		ArrayList<Excursion> excursiones = new ArrayList<Excursion>();
		for (Excursion e : exc) {
			excursiones.add(e);
		}
		Excursion excursion1 = excursiones.get(0);
		Assertions.assertTrue(excursion1.getTitulo().equals("Prueba1"));
	}

	@Test
	void debeEncontrarTodasLasExcursionesPublicadasYFuturas() {
		Iterable<Excursion> exc = this.excursionService.findAllPublished();

		ArrayList<Excursion> excursiones = new ArrayList<Excursion>();
		for (Excursion e : exc) {
			excursiones.add(e);
		}

		Excursion excursion = excursiones.get(0);
		Assertions.assertTrue(excursion.getTitulo().equals("Prueba3"));
	}

	@Test
	void debeEncontrarTodasLasExcursionesPorOrganizador() {
		Organizador organizador = this.organizadorService.findOrganizadorById(1);
		Iterable<Excursion> exc = this.excursionService.findAllMine(organizador);

		ArrayList<Excursion> excursiones = new ArrayList<Excursion>();
		for (Excursion e : exc) {
			excursiones.add(e);
		}
		Excursion excursion1 = excursiones.get(0);
		Assertions.assertTrue(excursion1.getTitulo().equals("Prueba1"));
	}

	@Test
	@Transactional
	public void debeCrearExcursion() {
		Organizador organizador = this.organizadorService.findOrganizadorById(1);
		Iterable<Excursion> exc1 = this.excursionService.findAllMine(organizador);
		ArrayList<Excursion> excursiones1 = new ArrayList<Excursion>();
		for (Excursion b : exc1) {
			excursiones1.add(b);
		}

		int total = excursiones1.size();

		Excursion excursion = new Excursion();
		excursion.setTitulo("Prueba");
		excursion.setDescripcion("Prueba desc");
		excursion.setFechaInicio(java.sql.Date.valueOf(LocalDate.now().plusDays(9)));
		excursion.setFechaFin(java.sql.Date.valueOf(LocalDate.now().plusDays(10)));
		excursion.setHoraInicio(LocalTime.of(9, 0));
		excursion.setHoraFin(LocalTime.of(20, 0));
		excursion.setRatioAceptacion(1.0);
		excursion.setNumeroResidencias(5);
		excursion.setOrganizador(organizador);
		excursion.setFinalMode(false);

		this.excursionService.saveExcursion(excursion);

		Iterable<Excursion> exc2 = this.excursionService.findAllMine(organizador);
		ArrayList<Excursion> excursiones2 = new ArrayList<Excursion>();
		for (Excursion e : exc2) {
			excursiones2.add(e);
		}

		Assertions.assertTrue(excursiones2.size() == total + 1);

		Assertions.assertTrue(excursion.getId() != null);
	}

	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoExcursionEnBlanco() {

		Excursion exc = new Excursion();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.excursionService.saveExcursion(exc);
		});
	}

		@Test
		@Transactional
		public void debeEliminarExcursion() {
			Organizador organizador = this.organizadorService.findOrganizadorById(1);
			Iterable<Excursion> exc1 = this.excursionService.findAllMine(organizador);
			ArrayList<Excursion> excursiones1 = new ArrayList<Excursion>();
			for (Excursion b : exc1) {
				excursiones1.add(b);
			}
	
			int total = excursiones1.size();
	
			Excursion excursion = this.excursionService.findExcursionById(2);
			this.excursionService.deleteExcursion(excursion);;
	
			Iterable<Excursion> exc2 = this.excursionService.findAllMine(organizador);
			if(exc2 != null) {
				ArrayList<Excursion> excursiones2 = new ArrayList<Excursion>();
				for (Excursion e : exc2) {
					excursiones2.add(e);
	
				}
				Assertions.assertTrue(excursiones2.size() == total - 1);
	
			}else {
				Assertions.assertTrue(exc2 == null);
			}	
		}

	@Test
	void debeEncontrarTodasLasExcursionesPorAnciano() {
		Anciano anciano = this.ancianoService.findAncianoById(7);
		Iterable<Excursion> exc = this.excursionService.findAllMineAnciano(anciano);

		ArrayList<Excursion> excursiones = new ArrayList<Excursion>();
		for (Excursion e : exc) {
			excursiones.add(e);
		}
		Excursion excursion1 = excursiones.get(0);
		Assertions.assertTrue(excursion1.getTitulo().equals("Prueba3"));
	}
	
	@Test
	@Transactional
	public void debeContarTodasLasExcursiones() {
		Assertions.assertTrue(this.excursionService.countExcursiones().equals(10L));
	}
	
	@Test
	@Transactional
	public void noDebeContarTodasLasExcursiones() {
		Assertions.assertTrue(!this.excursionService.countExcursiones().equals(4L));
	}
	
	@Test
	@Transactional
	public void debeHacerMediaExcursionesPorOrganizador() {
		Assertions.assertTrue(this.excursionService.avgExcursionesByOrganizador().equals(3.3333333333333335));
	}
	
	@Test
	@Transactional
	public void noDebeHacerMediaExcursionesPorOrganizador() {
		Assertions.assertTrue(!this.excursionService.avgExcursionesByOrganizador().equals(0.4));
	}

}
