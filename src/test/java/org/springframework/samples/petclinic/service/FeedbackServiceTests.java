
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.Feedback;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class FeedbackServiceTests {

	@Autowired
	protected FeedbackService			feedbackService;

	@Autowired
	protected ManagerService			managerService;
	
	@Autowired
	protected ResidenciaService			residenciaService;

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
	//TEST: ENCONTRAR FEEDBACK A PARTIR DE ID
	//##################################################################################################################
		
	//CASO POSITIVO: ENCUENTRA FEEDBACK A PARTIR DE ID CORRECTAMENTE
	@Test
	void debeEncontrarFeedbackConIdCorrecto() {
		Feedback fe = this.feedbackService.findFeedbackById(1);
		Assertions.assertTrue(fe.isDescartaFeedback() == false);
		Assertions.assertTrue(fe.getDescripcion().equals("desc1"));
		Assertions.assertTrue(fe.getValoracion().equals(2));
	}
	
	//CASO NEGATIVO: ENCUENTRA FEEDBACK A PARTIR DE ID QUE NO EXISTE
	  @Test 
	  void noDebeEncontrarFeedbackConIdInexistente() { 
		  Feedback fe = this.feedbackService.findFeedbackById(99999); 
		  Assertions.assertNull(fe); 
	}
	  
	//##################################################################################################################
	//TEST: ENCONTRAR TODOS LOS FEEDBACKS DADOS A UN ORGANIZADOR
	//##################################################################################################################
	
	  //CASO POSITIVO: ENCONTRAR TODOS LOS FEEDBACKS DADOS A UN ORGANIZADOR CORRECTAMENTE
	  @Test 
	  void debeEncontrarTodosMisFeedbacks() { 
		  Organizador organizador = this.organizadorService.findOrganizadorById(1);
		  Iterable<Feedback> fes = this.feedbackService.findAllMineOrganizador(organizador);
			  
		ArrayList<Feedback> fesis = new ArrayList<Feedback>(); 
		for (Feedback r : fes){ 
			fesis.add(r); 
		} 
		Feedback fe2 = fesis.get(0);
		Assertions.assertTrue(fe2.getDescripcion().equals("desc1")); 
		}
	  
	  //CASO NEGATIVO I: NO ENCUENTRA FEEDBACK CORRECTAMENTE POR NO HACER MATCH CON UN ATRIBUTO 
	  @Test 
	  void noDebeEncontrarFeedbackNoEsMio() { 
		  Organizador organizador = this.organizadorService.findOrganizadorById(1);
		  Iterable<Feedback> fes = this.feedbackService.findAllMineOrganizador(organizador);
			  
		ArrayList<Feedback> fesis = new ArrayList<Feedback>(); 
		for (Feedback r : fes){ 
			fesis.add(r); 
		} 
		Feedback fe2 = fesis.get(0);
		Assertions.assertFalse(fe2.getDescripcion().equals("descNOEXISTE")); 
		}
	 
	  //CASO NEGATIVO II: NO ENCUENTRA FEEDBACK CORRECTAMENTE POR RECIBIR COMO PARÁMETRO UN ORGANIZADOR IGUAL A NULL
	  @Test 
	  void noDebeEncontrarTodosMisFeedbacksSiOrganizadorNull() { 
		  Assertions.assertThrows(NullPointerException.class, () -> {
			  this.feedbackService.findAllMineOrganizador(null);
			});
	  }
	  
	//##################################################################################################################
	//TEST: CREAR FEEDBACK
	//##################################################################################################################	  
	  
	//CASO POSITIVO: CREA FEEDBACK CORRECTAMENTE
		@Test
		@Transactional
		public void debeCrearFeedback() {
			Manager m = this.managerService.findManagerById(7);
			Organizador o = this.organizadorService.findOrganizadorById(1);

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
			
			Excursion excursion = new Excursion();
			excursion.setTitulo("Prueba");
			excursion.setDescripcion("Prueba desc");
			excursion.setFechaInicio(java.sql.Date.valueOf(LocalDate.now().minusDays(9)));
			excursion.setFechaFin(java.sql.Date.valueOf(LocalDate.now().minusDays(10)));
			excursion.setHoraInicio(LocalTime.of(9, 0));
			excursion.setHoraFin(LocalTime.of(20, 0));
			excursion.setRatioAceptacion(1.0);
			excursion.setNumeroResidencias(5);
			excursion.setOrganizador(o);
			excursion.setFinalMode(true);
			this.excursionService.saveExcursion(excursion);
			
			PeticionExcursion petex = new PeticionExcursion();
			petex.setDeclaracion("Declaracion de prueba");
			petex.setEstado("aceptada");
			petex.setFecha(new Date(System.currentTimeMillis() - 1));
			petex.setExcursion(excursion);
			petex.setResidencia(rres2);
			this.peticionExcursionService.save(petex);
			
			Feedback feedback = new Feedback();
			feedback.setDescartaFeedback(false);
			feedback.setDescripcion("desc");
			feedback.setExcursion(excursion);
			feedback.setResidencia(rres2);
			feedback.setValoracion(4);
			this.feedbackService.save(feedback);
			Feedback fed = this.feedbackService.findFeedbackById(feedback.getId());

				
			Assertions.assertTrue(fed != null);
			Assertions.assertTrue(fed.getId() != null);
		}
		
		//CASO NEGATIVO: CREA FEEDBACK CON CAMPOS DEL FORMULARIO EN BLANCO
		@Test
		@Transactional
		public void debeLanzarExcepcionCreandoFeedbackEnBlanco() {
			Feedback fe = new Feedback();
			Assertions.assertThrows(ConstraintViolationException.class, () -> {
				this.feedbackService.save(fe);
			});
		}
	  
	//##################################################################################################################
	//TEST: CONTAR FEEDBACK DADA LA ID DE UNA EXCURSION Y LA DE UN MANAGER
	//##################################################################################################################	  
		  
	//CASO POSITIVO: CUENTA FEEDBACK DADA LA ID DE UNA EXCURSION Y LA DE UN MANAGER CORRECTAMENTE  
	  @Test 
	  void cuentaFeedbacksPorExcursionYManager() { 
		  Excursion ex = this.excursionService.findExcursionById(6);
		  Manager man = this.managerService.findManagerById(3);
		  int fe = this.feedbackService.countFeedbacksByExcursion(ex,man); 
		  Assertions.assertTrue(fe==1); 
	}
	  
	//CASO NEGATIVO I: CUENTA FEEDBACK SIN EXCURSION ASOCIADA 
	  @Test 
	  void noDebeContarFeedbacksSinExcursion() { 
		  Manager man = this.managerService.findManagerById(3);
		  int fe = this.feedbackService.countFeedbacksByExcursion(null,man);
		  Assertions.assertTrue(fe==0); 
	  }	
	  
	//CASO NEGATIVO II: CUENTA FEEDBACK SIN MANAGER ASOCIADO
	  @Test 
	  void noDebeContarFeedbacksSinManager() { 
		  Excursion ex = this.excursionService.findExcursionById(6);
		  int fe = this.feedbackService.countFeedbacksByExcursion(ex,null); 
		  Assertions.assertTrue(fe==0); 
	}	

	//CASO NEGATIVO III: CUENTA FEEDBACK SIN MANAGER Y SIN EXCURSION ASOCIADOS
	  @Test 
	  void noDebeContarFeedbacksSinManagerYSinExcursion() { 
		int fe = this.feedbackService.countFeedbacksByExcursion(null,null); 
		  Assertions.assertTrue(fe==0); 		
	}	

	//##################################################################################################################
	//TEST: CONTAR FEEDBACKS
	//##################################################################################################################
	
	//CASO POSITIVO: CUENTA FEEDBACK CORRECTAMENTE  
	 @Test
	 @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	 @Transactional
	 void cuentaFeedbacks() { 
	  	Long fe = this.feedbackService.countFeedbacks(); 
	  	Assertions.assertTrue(fe==2); 	
	}

	//CASO NEGATIVO: CUENTA FEEDBACK CORRECTAMENTE  
	 	@Test 
		 void noDebeContarFeedbacksIncorrectamente() { 
		  	Long fe = this.feedbackService.countFeedbacks(); 
		  	Assertions.assertFalse(fe==67); 	
	}

	//##################################################################################################################
	//TEST: CALCULAR MEDIA DE FEEDBACKS POR EXCURSION
	//##################################################################################################################
			
	//CASO POSITIVO: CUENTA FEEDBACK CORRECTAMENTE  
	@Test 
		void calcularMediaFeedbacksPorExcursion() { 
			 Double fe = this.feedbackService.avgFeedbacksByExcursion(); 
			 Assertions.assertTrue(fe==0.2); 	
		}

	//CASO NEGATIVO: CUENTA FEEDBACK CORRECTAMENTE  
	@Test 
		void noDebeCalcularMediaFeedbacksPorExcursionIncorrectamente() { 
			Double fe = this.feedbackService.avgFeedbacksByExcursion(); 
			Assertions.assertFalse(fe==67); 	
		}
}
