
package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Feedback;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.FeedbackService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = FeedbackController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class FeedbackControllerTest {

	private static final int		TEST_FEEDBACK_ID	= 1;
	private static final int		TEST_EXCURSION_ID	= 8;
	private static final int		TEST_EXCURSION_NO_FINALMODE_ID	= 9;
	private static final int		TEST_EXCURSION_NO_CADUCADA_ID	= 10;
	private static final int		TEST_EXCURSION_NO_ACEPTADA_ID	= 11;
	private static final int		TEST_PETICION_EXCURSION_ID	= 6;
	private static final int		TEST_PETICION_EXCURSION_RECHAZADA_ID	= 8;
	private static final int		TEST_RESIDENCIA_ID = 1;


	private static final String		TEST_MANAGER_NOMBRE		= "manager";
	private static final String		TEST_ORGANIZADOR_NOMBRE		= "organizador";
	private static final String		TEST_ANCIANO_NOMBRE		= "anciano";

	@MockBean
	private FeedbackService		feedbackService;

	@MockBean
	private ManagerService			managerService;

	@MockBean
	private ExcursionService		excursionesService;

	@MockBean
	private InscripcionService	inscripcionService;

	@MockBean
	private OrganizadorService	organizadorService;
	
	@MockBean
	private AuthoritiesService			authoritiesService;
	
	@MockBean
	private PeticionExcursionService peticionExcursionService;
	
	@MockBean
	private ResidenciaService	residenciaService;

	@Autowired
	private MockMvc					mockMvc;

	private Feedback				fe;
	private Excursion				ex;
	private Excursion				noFinal;
	private Excursion				noCaducada;
	private Excursion				noAceptada;
	private PeticionExcursion		pet;
	private PeticionExcursion		rech;
	private Residencia				re;
	Manager							man						= new Manager();
	Organizador 					organ 					= new Organizador();
	Anciano 						anc 					= new Anciano();
	User							user					= new User();
	User							userOrganiza			= new User();
	User							userMan2					= new User();
	User							userOrg2					= new User();
	User							userAnc					= new User();
	Manager							man2						= new Manager();
	Organizador						org2						= new Organizador();
	
	@BeforeEach
	void setup() {
		this.user.setUsername(FeedbackControllerTest.TEST_MANAGER_NOMBRE);
		this.userOrganiza.setUsername(FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE);
		this.userAnc.setUsername(FeedbackControllerTest.TEST_ANCIANO_NOMBRE);
		
		this.man.setUser(this.user);
		this.organ.setUser(this.userOrganiza);
		this.userMan2.setUsername("managerIncorrecto");
		this.man2.setUser(this.userMan2);
		this.org2.setUser(this.userOrg2);
		this.userOrg2.setUsername("organizadorIncorrecto");
		this.anc.setUser(this.userAnc);
		
		//Residencia
		this.re = new Residencia();
		this.re.setId(FeedbackControllerTest.TEST_RESIDENCIA_ID);
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		this.re.setAceptaDependenciaGrave(false);
		this.re.setAforo(100);
		this.re.setCorreo("residencia1@mail.es");
		this.re.setDescripcion("Descripcion de prueba");
		this.re.setDireccion("Direccion");
		this.re.setEdadMaxima(70);
		this.re.setHoraApertura(horaApertura);
		this.re.setHoraCierre(horaCierre);
		this.re.setMasInfo("http://www.resi1.com");
		this.re.setNombre("Reidencia 1");
		this.re.setTelefono("987654321");
		this.re.setManager(this.man);
		this.residenciaService.saveResidencia(this.re);
		
		//Excursion
		this.ex = new Excursion();
		this.ex.setId(FeedbackControllerTest.TEST_EXCURSION_ID);
		this.ex.setTitulo("Prueba");
		this.ex.setDescripcion("Prueba desc");
		this.ex.setFechaInicio(java.sql.Date.valueOf(LocalDate.now().minusDays(9)));
		this.ex.setFechaFin(java.sql.Date.valueOf(LocalDate.now().minusDays(10)));
		this.ex.setHoraInicio(LocalTime.of(9, 0));
		this.ex.setHoraFin(LocalTime.of(20, 0));
		this.ex.setRatioAceptacion(0.1);
		this.ex.setNumeroResidencias(15);
		this.ex.setOrganizador(this.organ);
		this.ex.setFinalMode(true);
		this.excursionesService.saveExcursion(this.ex);
		
		//Excursion NO FINAL MODE
		this.noFinal = new Excursion();
		this.noFinal.setId(FeedbackControllerTest.TEST_EXCURSION_NO_FINALMODE_ID);
		this.noFinal.setTitulo("Prueba");
		this.noFinal.setDescripcion("Prueba desc");
		this.noFinal.setFechaInicio(java.sql.Date.valueOf(LocalDate.now().minusDays(9)));
		this.noFinal.setFechaFin(java.sql.Date.valueOf(LocalDate.now().minusDays(10)));
		this.noFinal.setHoraInicio(LocalTime.of(9, 0));
		this.noFinal.setHoraFin(LocalTime.of(20, 0));
		this.noFinal.setRatioAceptacion(0.1);
		this.noFinal.setNumeroResidencias(15);
		this.noFinal.setOrganizador(this.organ);
		this.noFinal.setFinalMode(false);
		this.excursionesService.saveExcursion(this.noFinal);
		
		//Excursion NO CADUCADA
		this.noCaducada = new Excursion();
		this.noCaducada.setId(FeedbackControllerTest.TEST_EXCURSION_NO_CADUCADA_ID);
		this.noCaducada.setTitulo("Prueba");
		this.noCaducada.setDescripcion("Prueba desc");
		this.noCaducada.setFechaInicio(java.sql.Date.valueOf(LocalDate.now().plusDays(9)));
		this.noCaducada.setFechaFin(java.sql.Date.valueOf(LocalDate.now().plusDays(10)));
		this.noCaducada.setHoraInicio(LocalTime.of(9, 0));
		this.noCaducada.setHoraFin(LocalTime.of(20, 0));
		this.noCaducada.setRatioAceptacion(0.1);
		this.noCaducada.setNumeroResidencias(15);
		this.noCaducada.setOrganizador(this.organ);
		this.noCaducada.setFinalMode(false);
		this.excursionesService.saveExcursion(this.noCaducada);
		
		//Excursion NO ACEPTADA
		this.noAceptada = new Excursion();
		this.noAceptada.setId(FeedbackControllerTest.TEST_EXCURSION_NO_ACEPTADA_ID);
		this.noAceptada.setTitulo("Prueba");
		this.noAceptada.setDescripcion("Prueba desc");
		this.noAceptada.setFechaInicio(java.sql.Date.valueOf(LocalDate.now().minusDays(9)));
		this.noAceptada.setFechaFin(java.sql.Date.valueOf(LocalDate.now().minusDays(10)));
		this.noAceptada.setHoraInicio(LocalTime.of(9, 0));
		this.noAceptada.setHoraFin(LocalTime.of(20, 0));
		this.noAceptada.setRatioAceptacion(0.1);
		this.noAceptada.setNumeroResidencias(15);
		this.noAceptada.setOrganizador(this.organ);
		this.noAceptada.setFinalMode(true);
		this.excursionesService.saveExcursion(this.noAceptada);
		
		//PeticionExcursion
		this.pet = new PeticionExcursion();
		this.pet.setId(FeedbackControllerTest.TEST_PETICION_EXCURSION_ID);
		this.pet.setDeclaracion("Declaracion de prueba");
		this.pet.setEstado("aceptada");
		this.pet.setFecha(java.sql.Date.valueOf(LocalDate.now().minusDays(12)));
		this.pet.setExcursion(this.ex);
		this.pet.setResidencia(this.re);
		this.pet.setJustificacion(null);
		this.peticionExcursionService.save(this.pet);
		
		//PeticionExcursion RECHAZADA
		this.rech = new PeticionExcursion();
		this.rech.setId(FeedbackControllerTest.TEST_PETICION_EXCURSION_RECHAZADA_ID);
		this.rech.setDeclaracion("Declaracion de prueba");
		this.rech.setEstado("rechazada");
		this.rech.setFecha(java.sql.Date.valueOf(LocalDate.now().minusDays(12)));
		this.rech.setExcursion(this.noAceptada);
		this.rech.setResidencia(this.re);
		this.rech.setJustificacion(null);
		this.peticionExcursionService.save(this.rech);
		
		//Feedback
		this.fe = new Feedback();
		this.fe.setId(FeedbackControllerTest.TEST_FEEDBACK_ID);
		this.fe.setDescripcion("Descripcion de prueba");
		this.fe.setValoracion(2);
		this.fe.setDescartaFeedback(false);
		this.fe.setExcursion(this.ex);
		this.fe.setResidencia(this.re);
		this.feedbackService.save(this.fe);
		
		BDDMockito.given(this.feedbackService.findFeedbackById(FeedbackControllerTest.TEST_FEEDBACK_ID)).willReturn(this.fe);
		BDDMockito.given(this.excursionesService.findExcursionById(FeedbackControllerTest.TEST_EXCURSION_ID)).willReturn(this.ex);
		BDDMockito.given(this.excursionesService.findExcursionById(FeedbackControllerTest.TEST_EXCURSION_NO_FINALMODE_ID)).willReturn(this.noFinal);
		BDDMockito.given(this.excursionesService.findExcursionById(FeedbackControllerTest.TEST_EXCURSION_NO_CADUCADA_ID)).willReturn(this.noCaducada);
		BDDMockito.given(this.excursionesService.findExcursionById(FeedbackControllerTest.TEST_EXCURSION_NO_ACEPTADA_ID)).willReturn(this.noAceptada);

		BDDMockito.given(this.peticionExcursionService.findPeticionExcursionById(FeedbackControllerTest.TEST_PETICION_EXCURSION_ID)).willReturn(this.pet);
		BDDMockito.given(this.peticionExcursionService.findPeticionExcursionById(FeedbackControllerTest.TEST_PETICION_EXCURSION_RECHAZADA_ID)).willReturn(this.rech);

		BDDMockito.given(this.managerService.findManagerByUsername(FeedbackControllerTest.TEST_MANAGER_NOMBRE)).willReturn(this.man);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername(FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn(this.organ);
		
		BDDMockito.given(this.residenciaService.findResidenciaById(FeedbackControllerTest.TEST_RESIDENCIA_ID)).willReturn(this.re);
		BDDMockito.given(this.managerService.findResidenciaByManagerUsername(FeedbackControllerTest.TEST_MANAGER_NOMBRE)).willReturn(this.re);
		BDDMockito.given(this.managerService.findManagerByUsername("managerIncorrecto")).willReturn(this.man2);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername("organizadorIncorrecto")).willReturn(this.org2);

		BDDMockito.given(this.authoritiesService.findAuthority(FeedbackControllerTest.TEST_MANAGER_NOMBRE)).willReturn("manager");
		BDDMockito.given(this.authoritiesService.findAuthority(FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn("organizador");

		BDDMockito.given(this.feedbackService.countFeedbacksByExcursion(this.ex,this.man)).willReturn(0);
		BDDMockito.given(this.peticionExcursionService.countPeticionesByExcursionAceptada(this.ex,this.man)).willReturn(1);
		}
	
	  @WithMockUser(username = FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)
	  @Test 
	  void testProcessFindFormSuccessOrganizador() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("feedbacks/feedbackList")); 
	  }
	  
	  //manager intenta acceder al listado de feedbacks
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test 
	  void testProcessFindFormErrorManager() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks"))
	  .andExpect(MockMvcResultMatchers.view().name("exception")); 
	  }
	  
	  //anciano intenta acceder al listado de feedbacks
	  @WithMockUser(username = FeedbackControllerTest.TEST_ANCIANO_NOMBRE)
	  @Test 
	  void testProcessFindFormErrorAnciano() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks"))
	  .andExpect(MockMvcResultMatchers.view().name("exception")); 
	  }	  
	  
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
	  void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("feedbacks/createOrUpdateFeedbackForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("feedback"));
	  }
	  
	  //no debe poder acceder al form de crear un feedback un manager distinto al que hizo la petición de excursión aceptada
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
	  void testInitCreationFormErrorManagerIncorrecto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	  
	  //no debe poder acceder al form de crear un feedback un organizador
	  @WithMockUser(username = FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)
	  @Test
	  void testInitCreationFormErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	  //no debe poder acceder al form de crear un feedback un anciano
	  @WithMockUser(username = FeedbackControllerTest.TEST_ANCIANO_NOMBRE)
	  @Test
	  void testInitCreationFormErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	  
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
		void testProcessCreationFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.status()
						.isOk()).andExpect(MockMvcResultMatchers.view()
						.name("feedbacks/createOrUpdateFeedbackForm"));
		}
	  
	  //no debe poder enviar el form de crear un feedback un manager distinto al que hizo la petición de excursión aceptada
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
		void testProcessCreationFormErrorManagerIncorrecto() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}	  
	  
	  //no debe poder enviar el form de crear un feedback un anciano
	  @WithMockUser(username = FeedbackControllerTest.TEST_ANCIANO_NOMBRE)
	  @Test
		void testProcessCreationFormErrorAnciano() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}	  
	  
	  //no debe poder enviar el form de crear un feedback un organizador
	  @WithMockUser(username = FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)
	  @Test
		void testProcessCreationFormErrorOrganizador() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}	  
	 
	  //no debe poder acceder al form de crear un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
	  void testInitCreationFormErrorNoFinalModeExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_FINALMODE_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	  }
	  
	  //no debe poder acceder al form de crear un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
	  void testInitCreationFormErrorManagerIncorrectoNoFinalModeExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_FINALMODE_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
		void testProcessCreationFormErrorNoFinalModeExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_FINALMODE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));

		}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
		void testProcessCreationFormErrorManagerIncorrectoNoFinalModeExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_FINALMODE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}	  
	
	  //no debe poder acceder al form de crear un feedback para una excursión que no está caducada
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
	  void testInitCreationFormErrorNoCaducadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_CADUCADA_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	  }
	  
	  //no debe poder acceder al form de crear un feedback para una excursión que no está caducada
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
	  void testInitCreationFormErrorManagerIncorrectoNoCaducadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_CADUCADA_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está caducada
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
		void testProcessCreationFormErrorNoCaducadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_CADUCADA_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está caducada
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
		void testProcessCreationFormErrorManagerIncorrectoNoCaducadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_CADUCADA_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}	  	  
	  
	  //no debe poder acceder al form de crear un feedback para una excursión que no está aceptada
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
	  void testInitCreationFormErrorNoAceptadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_ACEPTADA_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	  }
	  
	  //no debe poder acceder al form de crear un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
	  void testInitCreationFormErrorManagerIncorrectoNoAceptadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_ACEPTADA_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está aceptada
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
		void testProcessCreationFormErrorNoAceptadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_ACEPTADA_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
		void testProcessCreationFormErrorManagerIncorrectoNoAceptadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", FeedbackControllerTest.TEST_EXCURSION_NO_ACEPTADA_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	 	  
	  @WithMockUser(username = FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)
	  @Test
	  void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(model().attributeExists("feedback"))
		.andExpect(model().attribute("feedback", hasProperty("descripcion", is("Descripcion de prueba"))))
		.andExpect(model().attribute("feedback", hasProperty("valoracion", is(2))))
		.andExpect(model().attribute("feedback", hasProperty("descartaFeedback", is(false))))
		.andExpect(view().name("feedbacks/createOrUpdateFeedbackForm"));
	  }
	  
	  //no debe poder acceder al form de actualizar un feedback un organizador distinto al que recibió el feedback
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
	  void testInitUpdateFormErrorOrganizadorIncorrecto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	  
	  //no debe poder acceder al form de actualizar un feedback un manager
	  @WithMockUser(username = FeedbackControllerTest.TEST_MANAGER_NOMBRE)
	  @Test
	  void testInitUpdateFormErrorManager() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	  //no debe poder acceder al form de actualizar un feedback un anciano
	  @WithMockUser(username = FeedbackControllerTest.TEST_ANCIANO_NOMBRE)
	  @Test
	  void testInitUpdateFormErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}	
	  
	  @WithMockUser(username = FeedbackControllerTest.TEST_ORGANIZADOR_NOMBRE)
	  @Test
		void testProcessUpdateFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.status().isOk());
		}
	  
	  //no debe poder enviar el form de update un feedback un organizador distinto al que recibió el feedback
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
		void testProcessUpdateFormErrorOrganizadorIncorrecto() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}	  
	  
	  //no debe poder enviar el form de update un feedback un anciano
	  @WithMockUser(username = FeedbackControllerTest.TEST_ANCIANO_NOMBRE)
	  @Test
		void testProcessUpdateFormErrorAnciano() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}	  

	  //no debe poder acceder al form de update un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
	  void testInitUpdateFormErrorManagerIncorrectoNoFinalModeExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	  
	  //no debe poder enviar el form de update un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
		void testProcessUpdateFormErrorManagerIncorrectoNoFinalModeExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}	  
	  
	  //no debe poder acceder al form de update un feedback para una excursión que no está caducada
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
	  void testInitUpdateFormErrorManagerIncorrectoNoCaducadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	  
	  //no debe poder enviar el form de update un feedback para una excursión que no está caducada
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
		void testProcessUpdateFormErrorManagerIncorrectoNoCaducadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}	  	  
	  
	  //no debe poder acceder al form de update un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
	  void testInitUpdateFormErrorManagerIncorrectoNoAceptadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	 
	  //no debe poder enviar el form de update un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
		void testProcessUpdateFormErrorManagerIncorrectoNoAceptadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", FeedbackControllerTest.TEST_FEEDBACK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
}