package org.springframework.samples.petclinic.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Inscripcion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class InscripcionServiceTests {

	@Autowired
	protected InscripcionService	inscripcionService;

	@Autowired
	protected ManagerService		managerService;

	@Autowired
	protected AncianoService		ancianoService;

	@Autowired
	protected ResidenciaService		residenciaService;

	//##################################################################################################################
	//TEST: ENCONTRAR INSCRIPCION A PARTIR DE ID
	//##################################################################################################################
	
	//CASO POSITIVO: ENCUENTRA INSCRIPCION CON ID CORRECTO
	@Test
	void debeEncontrarInscripcionConIdCorrecto() {
		Inscripcion i = this.inscripcionService.findInscripcionById(1);
		Assertions.assertTrue(i.getDeclaracion().equals("Declaracion1"));
	}
	
	//CASO NEGATIVO: ENCUENTRA INSCRIPCION CON ID QUE NO EXISTE	
	@Test
	void noDebeEncontrarInscripcionaConIdIncorrecto() {
		Inscripcion i = this.inscripcionService.findInscripcionById(99999);
		Assertions.assertNull(i);
	}
	
	//##################################################################################################################
	//TEST: CREAR INSCRIPCION A UNA RESIDENCIA
	//##################################################################################################################

		//CASO POSITIVO: CREA INSCRIPCION A UNA RESIDENCIA CORRECTAMENTE
		@Test
		@Transactional
		@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
		public void debeCrearInscripcionAResidencia() {
			Manager m = this.managerService.findManagerById(7);
			Anciano an = this.ancianoService.findAncianoById(21);
			
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

			
			Inscripcion in = new Inscripcion();
			in.setAnciano(an);
			in.setDeclaracion("declara");
			in.setEstado("pendiente");
			Calendar calendar2 = Calendar.getInstance();
			calendar2.set(2018, 11, 31, 12, 59, 59);
			Date fecha2 = calendar2.getTime();
			in.setFecha(fecha2);
			in.setJustificacion(null);
			in.setResidencia(rres2);
			this.inscripcionService.saveInscripcion(in);
			Inscripcion insc = this.inscripcionService.findInscripcionById(in.getId());
			
			Assertions.assertTrue(insc != null);
			Assertions.assertTrue(insc.getId() != null);
		}
		
		//CASO NEGATIVO: CREA INSCRIPCION CON CAMPOS DEL FORMULARIO EN BLANCO
		@Test
		@Transactional
		public void noDebeCrearInscripcionEnBlanco() {
			Inscripcion i = new Inscripcion();
			Assertions.assertThrows(ConstraintViolationException.class, () -> {
				this.inscripcionService.saveInscripcion(i);
			});
		}	
				
	//##################################################################################################################
	//TEST: ENCONTRAR TODAS LAS INSCRIPCIONES DE UN ANCIANO
	//##################################################################################################################
		
		  //CASO POSITIVO: ENCONTRAR TODAS LAS INSCRIPCIONES DE UN ANCIANO CORRECTAMENTE
		  @Test 
		  void debeEncontrarTodasMisInscripcionesAnciano() { 
			  Anciano anca = this.ancianoService.findAncianoById(11);
			  Iterable<Inscripcion> ins = this.inscripcionService.findAllMineAnciano(anca);
				  
			ArrayList<Inscripcion> inscrips = new ArrayList<Inscripcion>(); 
			for (Inscripcion i : ins){ 
				inscrips.add(i); 
			} 
			Inscripcion i2 = inscrips.get(0);
			Assertions.assertTrue(i2.getDeclaracion().equals("DeclaracionRS")); 
			}
		  
		  //CASO NEGATIVO I: NO ENCUENTRA INSCRIPCION CORRECTAMENTE POR NO HACER MATCH CON UN ATRIBUTO 
		  @Test
		  void noDebeEncontrarInscripcionIncorrectaAnciano() { 
			  Anciano anca = this.ancianoService.findAncianoById(11);
			  Iterable<Inscripcion> ins = this.inscripcionService.findAllMineAnciano(anca);
				  
			ArrayList<Inscripcion> inscrips = new ArrayList<Inscripcion>(); 
			for (Inscripcion i : ins){ 
				inscrips.add(i); 
			} 
			Inscripcion i2 = inscrips.get(0);
			Assertions.assertFalse(i2.getDeclaracion().equals("DeclaracionINCORRECTA")); 
			}
		 
		  //CASO NEGATIVO II: NO ENCUENTRA INSCRIPCION CORRECTAMENTE POR RECIBIR COMO PARÁMETRO UN ANCIANO IGUAL A NULL
		  @Test 
		  void noDebeEncontrarTodosMisInscripcionessSiAncianoNull() { 
			  Assertions.assertThrows(NullPointerException.class, () -> {
				  this.inscripcionService.findAllMineAnciano(null);
				});
			}		
	//##################################################################################################################
	//TEST: ENCONTRAR TODAS LAS INSCRIPCIONES DE UN MANAGER
	//##################################################################################################################
					  
	//CASO POSITIVO: ENCONTRAR TODAS LAS INSCRIPCIONES DE UN MANAGER CORRECTAMENTE
	@Test 
		void debeEncontrarTodasMisInscripcionesManager() { 
			Manager man = this.managerService.findManagerById(3);
			Iterable<Inscripcion> ins = this.inscripcionService.findAllMineManager(man);
			
			ArrayList<Inscripcion> inscr = new ArrayList<Inscripcion>(); 
			for (Inscripcion i : ins){ 
				inscr.add(i); 
			} 
			Inscripcion in2 = inscr.get(0);
			Assertions.assertTrue(in2.getDeclaracion().equals("Declaracion1")); 
	}	
		
	//CASO NEGATIVO I: NO ENCUENTRA INSCRIPCION CORRECTAMENTE POR NO HACER MATCH CON UN ATRIBUTO 
	@Test 
	void noDebeEncontrarInscripcionNoEsMia() { 
		Manager man = this.managerService.findManagerById(3);
		Iterable<Inscripcion> ins = this.inscripcionService.findAllMineManager(man);
		
		ArrayList<Inscripcion> inscr = new ArrayList<Inscripcion>(); 
		for (Inscripcion i : ins){ 
			inscr.add(i); 
		} 
		Inscripcion in2 = inscr.get(0);
		Assertions.assertFalse(in2.getDeclaracion().equals("DeclaracionINCORRECTA")); 
	}			

	 //CASO NEGATIVO II: NO ENCUENTRA INSCRIPCION CORRECTAMENTE POR RECIBIR COMO PARÁMETRO UN MANAGER IGUAL A NULL
	  @Test 
	  void noDebeEncontrarTodosMisInscripcionessSiManagerNull() { 
		  Assertions.assertThrows(NullPointerException.class, () -> {
			  this.inscripcionService.findAllMineManager(null);
			});
		}
	
	//##################################################################################################################
	//TESTS: CONTAR INSCRIPCIONES TOTALES Y EN LA RESIDENCIA
	//##################################################################################################################	  
			  
		//CASO POSITIVO: CUENTA INSCRIPCIONES ACEPTADAS TOTALES CORRECTAMENTE
		  @Test 
		  void cuentaInscripcionesAceptadas() { 
			  Long in = this.inscripcionService.countInscripcionesAceptadas();
			  Assertions.assertTrue(in==13); 
		}
		
		//CASO POSITIVO: CUENTA INSCRIPCIONES RECHAZADAS TOTALES CORRECTAMENTE
		  @Test 
		  void cuentaInscripcionesRechazadas() { 
			  Long in = this.inscripcionService.countInscripcionesRechazadas();
			  Assertions.assertTrue(in==1); 
		}
		  
		  //CASO POSITIVO: CUENTA INSCRIPCIONES TOTALES CORRECTAMENTE
		  @Test
		  @Transactional
		  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
		  void cuentaInscripcionesTotales() { 
			  Long in = this.inscripcionService.countInscripciones();
			  Assertions.assertTrue(in==16); 
		}
	
		//CASO NEGATIVO: NO CUENTA INSCRIPCIONES ACEPTADAS TOTALES CORRECTAMENTE
		  @Test 
		  void noDebeContarInscripcionesAceptadasIncorrectamente() { 
			  Long in = this.inscripcionService.countInscripcionesAceptadas();
			  Assertions.assertFalse(in==34); 
		}
		
		//CASO NEGATIVO: NO CUENTA INSCRIPCIONES RECHAZADAS TOTALES CORRECTAMENTE
		  @Test 
		  void noDebeContarInscripcionesRechadasIncorrectamente() { 
			  Long in = this.inscripcionService.countInscripcionesRechazadas();
			  Assertions.assertFalse(in==89); 
		}
		  
		  //CASO NEGATIVO: NO CUENTA INSCRIPCIONES TOTALES CORRECTAMENTE
		  @Test 
		  void noDebeContarInscripcionesTotalesIncorrectamente() { 
			  Long in = this.inscripcionService.countInscripciones();
			  Assertions.assertFalse(in==235); 
		}
	
		//CASO POSITIVO: CUENTA INSCRIPCIONES ACEPTADAS EN RESIDENCIA TOTALES CORRECTAMENTE
		  @Test 
		  void cuentaInscripcionesAceptadasResidencia() { 
			  Manager man = this.managerService.findManagerById(3);
			  Residencia res = this.residenciaService.findMine(man);
			  Integer in = this.inscripcionService.cuentaAceptadasEnResidencia(res);
			  Assertions.assertTrue(in==1); 
		}
	
		//CASO NEGATIVO: NO CUENTA INSCRIPCIONES ACEPTADAS EN RESIDENCIA TOTALES CORRECTAMENTE
		  @Test 
		  void noDebeContarInscripcionesAceptadasResidenciaIncorrectamente() { 
			  Manager man = this.managerService.findManagerById(3);
			  Residencia res = this.residenciaService.findMine(man);
			  Integer in = this.inscripcionService.cuentaAceptadasEnResidencia(res);
			  Assertions.assertFalse(in==1344); 
		}
	
	//##################################################################################################################
	//TESTS: ENCONTRAR TODAS LAS INSCRIPCIONES
	//##################################################################################################################
	
	//CASO POSITIVO: ENCUENTRA TODAS LAS INSCRIPCIONES CORRECTAMENTE
	@Test
	void debeEncontrarTodasLasInscripciones() {
		Iterable<Inscripcion> ins = this.inscripcionService.findAll();

		ArrayList<Inscripcion> insc = new ArrayList<Inscripcion>();
		for (Inscripcion i : ins) {
			insc.add(i);
		}
		Inscripcion in2 = insc.get(0);
		Assertions.assertTrue(in2.getDeclaracion().equals("Declaracion1"));
	}
	
	//CASO NEGATIVO: NO ENCUENTRA TODAS LAS INSCRIPCIONES CORRECTAMENTE
	@Test
	void noDebeEncontrarTodasLasInscripcionesIncorrectamente() {
		Iterable<Inscripcion> ins = this.inscripcionService.findAll();

		ArrayList<Inscripcion> insc = new ArrayList<Inscripcion>();
		for (Inscripcion i : ins) {
			insc.add(i);
		}
		Inscripcion in2 = insc.get(0);
		Assertions.assertFalse(in2.getDeclaracion().equals("DeclaracionINCORRECTA"));
	}	
	
	//##################################################################################################################
	//TESTS: CALCULAR RATIO DE LAS INSCRIPCIONES 
	//##################################################################################################################	
	
	//CASO POSTIVO: CALCULA RATIO DE LAS INSCRIPCIONES RECHAZADAS TOTALES CORRECTAMENTE
	 @Test
	 @Transactional
	 @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	  void calculaRatioInscripcionesRechazadasCorrectamente() { 
		  Double in = this.inscripcionService.ratioInscripcionesRechazadas();
		  Assertions.assertTrue(in==0.0625); 
	}  
	//CASO POSTIVO: CALCULA RATIO DE LAS INSCRIPCIONES ACEPTADAS TOTALES CORRECTAMENTE
	  @Test
	  @Transactional
	  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	  void calculaRatioInscripcionesAceptadasCorrectamente() { 
		  Double in = this.inscripcionService.ratioInscripcionesAceptadas();
		  Assertions.assertTrue(in==0.8125); 
	}
	  
	//CASO NEGATIVO: NO CALCULA RATIO DE LAS INSCRIPCIONES RECHAZADAS TOTALES CORRECTAMENTE
		@Test 
		 void noDebeCalcularRatioInscripcionesRechazadasIncorrectamente() { 
			  Double in = this.inscripcionService.ratioInscripcionesRechazadas();
			  Assertions.assertFalse(in==0.1895); 
	}  
	//CASO NEGATIVO: NO CALCULA RATIO DE LAS INSCRIPCIONES ACEPTADAS TOTALES CORRECTAMENTE
		 @Test 
		 void noDebeCalcularRatioInscripcionesAceptadasIncorrectamente() { 
			  Double in = this.inscripcionService.ratioInscripcionesAceptadas();
			  Assertions.assertFalse(in==0.8935); 
	}
}
