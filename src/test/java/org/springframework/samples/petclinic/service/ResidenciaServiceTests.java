package org.springframework.samples.petclinic.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Incidencia;
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
class ResidenciaServiceTests {

	@Autowired
	protected ResidenciaService			residenciaService;

	@Autowired
	protected ManagerService			managerService;

	@Autowired
	protected OrganizadorService		organizadorService;

	@Autowired
	protected IncidenciaService			incidenciaService;

	@Autowired
	protected BuenaAccionService		buenaAccionService;

	@Autowired
	protected ExcursionService			excursionService;

	@Autowired
	protected PeticionExcursionService	peticionExcursionService;

	@Autowired
	protected AncianoService			ancianoService;

	//##################################################################################################################
	//TEST: ENCONTRAR RESIDENCIA A PARTIR DE ID
	//##################################################################################################################
	
	//CASO POSITIVO: ENCUENTRA RESIDENCIA CON ID CORRECTO
	@Test
	void debeEncontrarResidenciaConIdCorrecto() {
		Residencia re = this.residenciaService.findResidenciaById(1);
		Assertions.assertTrue(re.getAceptaDependenciaGrave() == false);
		Assertions.assertTrue(re.getAforo().equals(100));
		Assertions.assertTrue(re.getCorreo().equals("residencia1@mail.es"));
		Assertions.assertTrue(re.getDescripcion().equals("Descripcion de prueba"));
		Assertions.assertTrue(re.getDireccion().equals("Direccion"));
		Assertions.assertTrue(re.getEdadMaxima().equals(70));
	}
	
	//CASO NEGATIVO: ENCUENTRA RESIDENCIA CON ID QUE NO EXISTE
	@Test
	void noDebeEncontrarResidenciaConIdIncorrecto() {
		Residencia re = this.residenciaService.findResidenciaById(99999);
		Assertions.assertNull(re);
	}

	//##################################################################################################################
	//TEST: CREAR RESIDENCIA
	//##################################################################################################################

	//CASO POSITIVO: CREA RESIDENCIA UN MANAGER QUE NO TENÍA PREVIAMENTE UNA REGISTRADA	
	@Test
	@Transactional
	public void debeCrearResidenciaManagerSinUna() {
		Manager m = this.managerService.findManagerById(7);

		Residencia ra = new Residencia();
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		ra.setAceptaDependenciaGrave(false);
		ra.setAforo(100);
		ra.setCorreo("residencia1@mail.es");
		ra.setDescripcion("Descripcion de prueba");
		ra.setDireccion("Direccion");
		ra.setEdadMaxima(70);
		ra.setHoraApertura(horaApertura);
		ra.setHoraCierre(horaCierre);
		ra.setMasInfo("http://www.resi1.com");
		ra.setNombre("Reidencia 1");
		ra.setTelefono("987654321");
		ra.setManager(m);

		this.residenciaService.saveResidencia(ra);

		Residencia rres2 = this.residenciaService.findMine(m);
		Assertions.assertTrue(rres2 != null);
		Assertions.assertTrue(ra.getId() != null);
	}
	
	//CASO NEGATIVO: CREA RESIDENCIA CON CAMPOS DEL FORMULARIO EN BLANCO
	@Test
	@Transactional
	public void debeLanzarExcepcionCreandoResidenciaEnBlanco() {
		Residencia ra = new Residencia();
		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			this.residenciaService.saveResidencia(ra);
		});
	}
	
	//##################################################################################################################
	//TEST: ENCONTRAR RESIDENCIA A PARTIR DE ID DE MANAGER
	//##################################################################################################################
	
	//CASO POSITIVO: ENCUENTRA RESIDENCIA CON ID DE MANAGER CORRECTO
	@Test
	void debeEncontrarResidenciaPorManager() {
		Manager m = this.managerService.findManagerById(3);
		Residencia res = this.residenciaService.findMine(m);
		Assertions.assertTrue(res.getCorreo().equals("residencia1@mail.es"));
	}

	//CASO NEGATIVO: ENCUENTRA RESIDENCIA CON ID DE MANAGER QUE NO EXISTE
	@Test
	void noDebeEncontrarResidenciaConIdManagerInexistente() {
		Manager m = this.managerService.findManagerById(9999999);
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.residenciaService.findMine(m);
		});
	}

	//##################################################################################################################
	//TEST: ENCONTRAR TODAS LAS RESIDENCIAS
	//##################################################################################################################

	//CASO POSITIVO: ENCUENTRA TODAS LAS RESIDENCIAS CORRECTAMENTE
	@Test
	void debeEncontrarTodasLasResidencias() {
		Iterable<Residencia> res = this.residenciaService.findAll();

		ArrayList<Residencia> resis = new ArrayList<Residencia>();
		for (Residencia r : res) {
			resis.add(r);
		}
		Residencia re2 = resis.get(0);
		Assertions.assertTrue(re2.getCorreo().equals("residencia1@mail.es"));
	}
	
	//CASO NEGATIVO: BUSCA RESIDENCIA FUERA DE LOS LÍMITES DE RESIDENCIAS
	@Test
	void noDebeEncontrarResidenciaInexistente() {
		Iterable<Residencia> res = this.residenciaService.findAll();

		ArrayList<Residencia> resis = new ArrayList<Residencia>();
		for (Residencia r : res) {
			resis.add(r);
		}
		
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			resis.get(9999999);
		});
	}

	//##################################################################################################################
	//TEST: CALCULAR RATIO DE BUENAS ACCIONES-INCIDENCIAS DE UNA RESIDENCIA
	//##################################################################################################################
	
	//CASO POSITIVO I: CALCULA EL RATIO CORRECTAMENTE DE UNA RESIDENCIA CON BUENAS ACCIONES E INCIDENCIAS
	@Test
	@Transactional
	public void debeCalcularRatio() {

		Residencia ra = new Residencia();
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		ra.setAceptaDependenciaGrave(false);
		ra.setAforo(100);
		ra.setCorreo("residencia1@mail.es");
		ra.setDescripcion("Descripcion de prueba");
		ra.setDireccion("Direccion");
		ra.setEdadMaxima(70);
		ra.setHoraApertura(horaApertura);
		ra.setHoraCierre(horaCierre);
		ra.setMasInfo("http://www.resi1.com");
		ra.setNombre("Reidencia 1");
		ra.setTelefono("987654321");
		this.residenciaService.saveResidencia(ra);

		//Primera incidencia
		Incidencia inci = new Incidencia();
		inci.setId(1);
		inci.setDescripcion("desc");
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, 11, 31, 12, 59, 59);
		Date fecha = calendar.getTime();
		inci.setFecha(fecha);
		inci.setResidencia(ra);
		inci.setTitulo("tit");
		this.incidenciaService.saveIncidencia(inci);

		//Segunda incidencia
		Incidencia inci2 = new Incidencia();
		inci2.setId(2);
		inci2.setDescripcion("desc");
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2018, 11, 31, 12, 59, 59);
		Date fecha2 = calendar2.getTime();
		inci.setFecha(fecha2);
		inci2.setResidencia(ra);
		inci2.setTitulo("tit");
		this.incidenciaService.saveIncidencia(inci2);

		//Primera buena acción
		BuenaAccion buena = new BuenaAccion();
		buena.setId(1);
		buena.setDescripcion("desc");
		Calendar calendar3 = Calendar.getInstance();
		calendar3.set(2018, 11, 31, 12, 59, 59);
		Date fecha3 = calendar3.getTime();
		buena.setFecha(fecha3);
		buena.setResidencia(ra);
		buena.setTitulo("tit");
		this.buenaAccionService.saveBuenaAccion(buena);

		Double m = this.residenciaService.getRatio(ra);
		Assertions.assertTrue(m == 0.5);
	}
	
	//CASO POSITIVO II: CALCULA EL RATIO CORRECTAMENTE DE UNA RESIDENCIA CON BUENAS ACCIONES Y SIN INCIDENCIAS
	@Test
	@Transactional
	public void debeCalcularRatioSinIncidencias() {

		Residencia ra = new Residencia();
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		ra.setAceptaDependenciaGrave(false);
		ra.setAforo(100);
		ra.setCorreo("residencia1@mail.es");
		ra.setDescripcion("Descripcion de prueba");
		ra.setDireccion("Direccion");
		ra.setEdadMaxima(70);
		ra.setHoraApertura(horaApertura);
		ra.setHoraCierre(horaCierre);
		ra.setMasInfo("http://www.resi1.com");
		ra.setNombre("Reidencia 1");
		ra.setTelefono("987654321");
		this.residenciaService.saveResidencia(ra);

		//Primera buena acción
		BuenaAccion buena = new BuenaAccion();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, 11, 31, 12, 59, 59);
		Date fechaDes = calendar.getTime();
		buena.setId(1);
		buena.setDescripcion("desc");
		buena.setFecha(fechaDes);
		buena.setResidencia(ra);
		buena.setTitulo("tit");
		this.buenaAccionService.saveBuenaAccion(buena);

		Double m = this.residenciaService.getRatio(ra);
		Assertions.assertTrue(m == 1);
	}

	//CASO POSITIVO III: CALCULA EL RATIO CORRECTAMENTE DE UNA RESIDENCIA SIN BUENAS ACCIONES Y CON INCIDENCIAS
	@Test
	@Transactional
	public void debeCalcularRatioSinBuenasAcciones() {

		Residencia ra = new Residencia();
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		ra.setAceptaDependenciaGrave(false);
		ra.setAforo(100);
		ra.setCorreo("residencia1@mail.es");
		ra.setDescripcion("Descripcion de prueba");
		ra.setDireccion("Direccion");
		ra.setEdadMaxima(70);
		ra.setHoraApertura(horaApertura);
		ra.setHoraCierre(horaCierre);
		ra.setMasInfo("http://www.resi1.com");
		ra.setNombre("Reidencia 1");
		ra.setTelefono("987654321");
		this.residenciaService.saveResidencia(ra);

		//Primera incidencia
		Incidencia inci = new Incidencia();
		inci.setId(1);
		inci.setDescripcion("desc");
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, 11, 31, 12, 59, 59);
		Date fechaInci = calendar.getTime();
		inci.setFecha(fechaInci);
		inci.setResidencia(ra);
		inci.setTitulo("tit");
		this.incidenciaService.saveIncidencia(inci);

		Double m = this.residenciaService.getRatio(ra);
		Assertions.assertTrue(m == 0);
	}
	
	//CASO POSITIVO IIII: DEVUELVE CERO EN EL RATIO SI NO HAY NI ACCIONES NI INCIDENCIAS
	@Test
	@Transactional
	public void debeCalcularRatioSinBuenasAccionesNiIncidencias() {

		Residencia ra = new Residencia();
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		ra.setAceptaDependenciaGrave(false);
		ra.setAforo(100);
		ra.setCorreo("residencia1@mail.es");
		ra.setDescripcion("Descripcion de prueba");
		ra.setDireccion("Direccion");
		ra.setEdadMaxima(70);
		ra.setHoraApertura(horaApertura);
		ra.setHoraCierre(horaCierre);
		ra.setMasInfo("http://www.resi1.com");
		ra.setNombre("Reidencia 1");
		ra.setTelefono("987654321");
		this.residenciaService.saveResidencia(ra);

		Double m = this.residenciaService.getRatio(ra);
		Assertions.assertTrue(m == 0);
	}
	
	//CASO NEGATIVO: NO DEBE CALCULAR RATIO SI RECIBE COMO RESIDENCIA UN NULL
	@Test
	public void noDebeCalcularRatioResidenciaNull() {
	Residencia nullRes = new Residencia();
	
	Assertions.assertThrows(NullPointerException.class, () -> {
		this.residenciaService.getRatio(nullRes);
		});
	}	
	
	//##################################################################################################################
	//TEST: ENCONTRAR RESIDENCIA TOP
	//##################################################################################################################
	
	//CASO POSITIVO I: ENCUENTRA RESIDENCIA TOP CORRECTAMENTE
	@Test
	@Transactional
	public void debeEncontrarResidenciaTop() {

		//RESIDENCIA 1
		Residencia RE1 = new Residencia();
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		RE1.setAceptaDependenciaGrave(false);
		RE1.setAforo(100);
		RE1.setCorreo("residencia1@mail.es");
		RE1.setDescripcion("Descripcion de prueba");
		RE1.setDireccion("Direccion");
		RE1.setEdadMaxima(70);
		RE1.setHoraApertura(horaApertura);
		RE1.setHoraCierre(horaCierre);
		RE1.setMasInfo("http://www.resi1.com");
		RE1.setNombre("Residencia Test Buena");
		RE1.setTelefono("987654321");
		this.residenciaService.saveResidencia(RE1);

		//RESIDENCIA 2
		Residencia RE2 = new Residencia();
		RE2.setAceptaDependenciaGrave(false);
		RE2.setAforo(102);
		RE2.setCorreo("residencia2@mail.es");
		RE2.setDescripcion("Descripcion de prueba");
		RE2.setDireccion("Direccion");
		RE2.setEdadMaxima(70);
		RE2.setHoraApertura(horaApertura);
		RE2.setHoraCierre(horaCierre);
		RE2.setMasInfo("http://www.resi2.com");
		RE2.setNombre("Residencia Test Mala");
		RE2.setTelefono("987654322");
		this.residenciaService.saveResidencia(RE2);

		//Primera incidencia RESIDENCIA 2
		Incidencia inci = new Incidencia();
		inci.setId(5);
		inci.setDescripcion("desc");
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, 11, 31, 12, 59, 59);
		Date fechaInciRE2UNO = calendar.getTime();
		inci.setFecha(fechaInciRE2UNO);
		inci.setResidencia(RE2);
		inci.setTitulo("tit");
		this.incidenciaService.saveIncidencia(inci);

		//Segunda incidencia RESIDENCIA 2
		Incidencia inci2 = new Incidencia();
		inci2.setId(6);
		inci2.setDescripcion("desc");
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2018, 11, 31, 12, 59, 59);
		Date fechaInciRE2DOS = calendar.getTime();
		inci2.setFecha(fechaInciRE2DOS);
		inci2.setResidencia(RE2);
		inci2.setTitulo("tit");
		this.incidenciaService.saveIncidencia(inci2);

		//Primera buena acción RESIDENCIA 1
		BuenaAccion buena = new BuenaAccion();
		buena.setId(4);
		buena.setDescripcion("desc");
		Calendar calendar3 = Calendar.getInstance();
		calendar3.set(2018, 11, 31, 12, 59, 59);
		Date fechaBUENARE1UNO = calendar.getTime();
		buena.setFecha(fechaBUENARE1UNO);
		buena.setResidencia(RE1);
		buena.setTitulo("tit");
		this.buenaAccionService.saveBuenaAccion(buena);

		//Segunda buena acción RESIDENCIA 1
		BuenaAccion buena2 = new BuenaAccion();
		buena2.setId(5);
		buena2.setDescripcion("desc");
		Calendar calendar4 = Calendar.getInstance();
		calendar4.set(2018, 11, 31, 12, 59, 59);
		Date fechaBuenaRE1DOS = calendar.getTime();
		buena2.setFecha(fechaBuenaRE1DOS);
		buena2.setResidencia(RE1);
		buena2.setTitulo("tit");
		this.buenaAccionService.saveBuenaAccion(buena2);

		//Tercera buena acción RESIDENCIA 1
		BuenaAccion buena3 = new BuenaAccion();
		buena3.setId(5);
		buena3.setDescripcion("desc");
		Calendar calendar5 = Calendar.getInstance();
		calendar5.set(2018, 11, 31, 12, 59, 59);
		Date fechaBuenaRE1TRES = calendar.getTime();
		buena3.setFecha(fechaBuenaRE1TRES);
		buena3.setResidencia(RE1);
		buena3.setTitulo("tit");
		this.buenaAccionService.saveBuenaAccion(buena3);

		//RE1 tiene 3 buenas acciones y 0 incidencias
		//RE2 tiene 0 buenas acciones y 2 incidencias

		//Comprobamos que RE1 está en el top 3 y que RE2 no está

		boolean RE1EnTop = false;
		boolean RE2EnTop = false;
		List<Residencia> resIterator = this.residenciaService.findTop(3);
		for (Residencia r : resIterator) {
			if (RE1.equals(r)) {
				RE1EnTop = true;
			} else if (RE2.equals(r)) {
				RE2EnTop = true;
			}
		}

		Assertions.assertTrue(RE1EnTop);
		Assertions.assertFalse(RE2EnTop);
	}
	
	//CASO POSITIVO II: DEVUELVE LISTA VACÍA SI HAY 0 RESULTADOS
	@Test
	public void debeDevolverListaVaciaSiNoHayResultados() {
		List<Residencia> resIterator = this.residenciaService.findTop(0);
		Assertions.assertTrue(resIterator.stream().collect(Collectors.toList()).isEmpty());
	}
	

	//CASO NEGATIVO: NO DEBE DEVOLVER UN ELEMENTO DE LA LISTA SI HAY 0 RESULTADOS
		@Test
		public void noDebeDevolverElementoListastaVaciaSiNoHayResultados() {
			List<Residencia> resIterator = this.residenciaService.findTop(0);
			Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
				resIterator.get(0);
				});
		}
		
	//##################################################################################################################
	//TEST: ENCONTRAR RESIDENCIAS SIN PARTICIPANTES
	//##################################################################################################################
		
	//CASO POSITIVO: ENCUENTRA CORRECTAMENTE RESIDENCIAS SIN PARTICIPANTES		
	@Test
	@Transactional
	public void debeEncontrarResidenciaSinParticipantes() {
		//RESIDENCIA 1
		Residencia ra = new Residencia();
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		ra.setAceptaDependenciaGrave(false);
		ra.setAforo(100);
		ra.setCorreo("residencia1@mail.es");
		ra.setDescripcion("Descripcion de prueba");
		ra.setDireccion("Direccion");
		ra.setEdadMaxima(70);
		ra.setHoraApertura(horaApertura);
		ra.setHoraCierre(horaCierre);
		ra.setMasInfo("http://www.resi1.com");
		ra.setNombre("Reidencia 1");
		ra.setTelefono("987654321");
		this.residenciaService.saveResidencia(ra);

		//RESIDENCIA 2 (sin peticiones)
		Residencia RE2 = new Residencia();
		LocalTime horaApertura2 = LocalTime.of(07, 00);
		LocalTime horaCierre2 = LocalTime.of(21, 00);
		RE2.setAceptaDependenciaGrave(false);
		RE2.setAforo(100);
		RE2.setCorreo("residencia2@mail.es");
		RE2.setDescripcion("Descripcion de prueba");
		RE2.setDireccion("Direccion");
		RE2.setEdadMaxima(70);
		RE2.setHoraApertura(horaApertura2);
		RE2.setHoraCierre(horaCierre2);
		RE2.setMasInfo("http://www.resi2.com");
		RE2.setNombre("Reidencia 2");
		RE2.setTelefono("987654322");
		this.residenciaService.saveResidencia(RE2);

		//Excursion 1
		Excursion ex1 = new Excursion();
		Organizador organizador1 = this.organizadorService.findOrganizadorById(1);
		ex1.setId(1);

		ex1.setDescripcion("desc");
		Calendar excursion1FechaInicio = Calendar.getInstance();
		excursion1FechaInicio.set(2018, 11, 31, 12, 59, 59);
		Date fechaInicio = excursion1FechaInicio.getTime();
		ex1.setFechaInicio(fechaInicio);

		Calendar excursion1FechaFin = Calendar.getInstance();
		excursion1FechaFin.set(2018, 12, 01, 13, 59, 59);
		Date fechaFin = excursion1FechaFin.getTime();
		ex1.setFechaFin(fechaFin);

		ex1.setFinalMode(true);

		ex1.setHoraInicio(LocalTime.of(12, 59));
		ex1.setHoraFin(LocalTime.of(13, 59));
		ex1.setRatioAceptacion(92.);
		ex1.setTitulo("tit");
		ex1.setNumeroResidencias(2);
		ex1.setOrganizador(organizador1);

		//Peticion 1
		PeticionExcursion insc = new PeticionExcursion();
		insc.setDeclaracion("decl");
		insc.setEstado("aceptado");
		insc.setExcursion(ex1);
		Calendar fechaPet = Calendar.getInstance();
		fechaPet.set(2018, 13, 01, 13, 59, 59);
		Date fechaPeticion = excursion1FechaFin.getTime();
		insc.setFecha(fechaPeticion);
		insc.setId(1);
		insc.setJustificacion(null);
		insc.setResidencia(ra);

		boolean r2EnNoParticipantes = false;
		Iterable<Residencia> resIterator = this.residenciaService.findResidenciasNoParticipantes(organizador1);
		for (Residencia r : resIterator) {
			if (RE2.equals(r)) {
				r2EnNoParticipantes = true;
				break;
			}
		}
		Assertions.assertTrue(r2EnNoParticipantes);
	}
	
	//CASO NEGATIVO: NO DEBE ENCONTRAR RESIDENCIAS SIN PARTICIPANTES CON ORGANIZADOR NULL
	@Test
	public void noDebeEncontrarResidenciaSinParticipantesConOrganizadorNull() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.residenciaService.findResidenciasNoParticipantes(null);
			});
	}

	//##################################################################################################################
	//TEST: ENCONTRAR RESIDENCIA A PARTIR DE ANCIANO DADO
	//##################################################################################################################
			
	//CASO POSITIVO: ENCUENTRA CORRECTAMENTE RESIDENCIA DADO UN ANCIANO		
	@Test
	void debeEncontrarResidenciaPorAnciano() {
		Anciano anciano = this.ancianoService.findAncianoById(7);
		Residencia residencia = this.residenciaService.findResidenciaByAnciano(anciano);
		Assertions.assertTrue(residencia.getAceptaDependenciaGrave() == false);
		Assertions.assertTrue(residencia.getAforo().equals(100));
		Assertions.assertTrue(residencia.getCorreo().equals("residencia1@mail.es"));
		Assertions.assertTrue(residencia.getDescripcion().equals("Descripcion de prueba"));
		Assertions.assertTrue(residencia.getDireccion().equals("Direccion"));
		Assertions.assertTrue(residencia.getEdadMaxima().equals(70));
	}
	
	//CASO NEGATIVO: NO ENCUENTRA RESIDENCIA DADO UN ANCIANO QUE NO EXISTE		
	@Test
	void noDebeEncontrarResidenciaPorAncianoSiNoExiste() {
		Anciano anciano = this.ancianoService.findAncianoById(9999);
		Assertions.assertNull(anciano);
	}
	
	//##################################################################################################################
	//TEST: CONTAR RESIDENCIAS EXSISTENTES
	//##################################################################################################################
				
	//CASO POSITIVO: CUENTA LAS RESIDENCIAS CORRECTAMENTE		
	@Test
	@Transactional
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	void debeContarResidencias() {
		//RESIDENCIA 1
				Residencia RE1 = new Residencia();
				LocalTime horaApertura = LocalTime.of(07, 00);
				LocalTime horaCierre = LocalTime.of(21, 00);
				RE1.setAceptaDependenciaGrave(false);
				RE1.setAforo(100);
				RE1.setCorreo("residencia1@mail.es");
				RE1.setDescripcion("Descripcion de prueba");
				RE1.setDireccion("Direccion");
				RE1.setEdadMaxima(70);
				RE1.setHoraApertura(horaApertura);
				RE1.setHoraCierre(horaCierre);
				RE1.setMasInfo("http://www.resi1.com");
				RE1.setNombre("Residencia Test Buena");
				RE1.setTelefono("987654321");
				this.residenciaService.saveResidencia(RE1);

				//RESIDENCIA 2
				Residencia RE2 = new Residencia();
				RE2.setAceptaDependenciaGrave(false);
				RE2.setAforo(102);
				RE2.setCorreo("residencia2@mail.es");
				RE2.setDescripcion("Descripcion de prueba");
				RE2.setDireccion("Direccion");
				RE2.setEdadMaxima(70);
				RE2.setHoraApertura(horaApertura);
				RE2.setHoraCierre(horaCierre);
				RE2.setMasInfo("http://www.resi2.com");
				RE2.setNombre("Residencia Test Mala");
				RE2.setTelefono("987654322");
				this.residenciaService.saveResidencia(RE2);
				
			Assertions.assertTrue(this.residenciaService.countResidencias()==6);
	}
		
	//CASO NEGATIVO: NO CUENTA LAS RESIDENCIAS CORRECTAMENTE	
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	void noCuentaResidenciasCorrectamente() {
		Assertions.assertFalse(this.residenciaService.countResidencias()!=4);
	}

	//##################################################################################################################
	//TEST: CONTAR RESIDENCIAS COMPLETAS EXISTENTES
	//##################################################################################################################
	
	//CASO POSITIVO: CUENTA RESIDENCIAS COMPLETAS EXISTENTES
	@Test
	void debeContarResidenciasCompletasCorrectamente() {
		Assertions.assertTrue(this.residenciaService.countResidenciasCompletas()==1);
	}
	
	//CASO NEGATIVO: NO CUENTA LAS RESIDENCIAS COMPLETAS CORRECTAMENTE	
	@Test
	void noCuentaResidenciasCompletasCorrectamente() {
		Assertions.assertFalse(this.residenciaService.countResidenciasCompletas()==12);
	}
}
