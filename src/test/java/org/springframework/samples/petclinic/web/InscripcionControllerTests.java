package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalTime;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Inscripcion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = InscripcionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class InscripcionControllerTests {

	private static final int		TEST_RESIDENCIA_ID = 1;
	private static final int		TEST_INSCRIPCION_ID = 1;

	private static final String		TEST_MANAGER_NOMBRE		= "manager";
	private static final String		TEST_ORGANIZADOR_NOMBRE		= "organizador";
	private static final String		TEST_ANCIANO_NOMBRE		= "anciano";

	@MockBean
	private InscripcionService	inscripcionService;
	@MockBean
	private ResidenciaService	residenciaService;	
	@MockBean
	private AncianoService			ancianoService;	
	@MockBean
	private ManagerService			managerService;
	@MockBean
	private AuthoritiesService			authoritiesService;
		
	@MockBean
	private OrganizadorService	organizadorService;

	@Autowired
	private MockMvc					mockMvc;
	
	private Residencia				re;
	private Inscripcion				in;
	Manager							man						= new Manager();
	private Date					hoy						= new Date(System.currentTimeMillis() - 1);
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
		this.user.setUsername(InscripcionControllerTests.TEST_MANAGER_NOMBRE);
		this.userOrganiza.setUsername(InscripcionControllerTests.TEST_ORGANIZADOR_NOMBRE);
		this.userAnc.setUsername(InscripcionControllerTests.TEST_ANCIANO_NOMBRE);
		this.userMan2.setUsername("managerIncorrecto");
		
		this.man.setUser(this.user);
		this.organ.setUser(this.userOrganiza);
		this.man2.setUser(this.userMan2);
		this.org2.setUser(this.userOrg2);
		this.userOrg2.setUsername("organizadorIncorrecto");
		this.anc.setUser(this.userAnc);
		
		//Residencia
		this.re = new Residencia();
		this.re.setId(InscripcionControllerTests.TEST_RESIDENCIA_ID);
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
		
		//Inscripcion
		this.in = new Inscripcion();
		this.in.setId(InscripcionControllerTests.TEST_INSCRIPCION_ID);
		this.in.setAnciano(this.anc);
		this.in.setDeclaracion("dec");
		this.in.setEstado("pendiente");
		this.in.setJustificacion("si");
		this.in.setResidencia(this.re);
		this.in.setFecha(this.hoy);
		this.inscripcionService.saveInscripcion(this.in);
		
		BDDMockito.given(this.managerService.findManagerByUsername(InscripcionControllerTests.TEST_MANAGER_NOMBRE)).willReturn(this.man);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername(InscripcionControllerTests.TEST_ORGANIZADOR_NOMBRE)).willReturn(this.organ);
		BDDMockito.given(this.ancianoService.findAncianoByUsername(InscripcionControllerTests.TEST_ANCIANO_NOMBRE)).willReturn(this.anc);

		BDDMockito.given(this.residenciaService.findResidenciaById(InscripcionControllerTests.TEST_RESIDENCIA_ID)).willReturn(this.re);
		BDDMockito.given(this.inscripcionService.findInscripcionById(InscripcionControllerTests.TEST_INSCRIPCION_ID)).willReturn(this.in);

		BDDMockito.given(this.managerService.findResidenciaByManagerUsername(InscripcionControllerTests.TEST_MANAGER_NOMBRE)).willReturn(this.re);
		BDDMockito.given(this.managerService.findManagerByUsername("managerIncorrecto")).willReturn(this.man2);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername("organizadorIncorrecto")).willReturn(this.org2);

		BDDMockito.given(this.authoritiesService.findAuthority(InscripcionControllerTests.TEST_MANAGER_NOMBRE)).willReturn("manager");
		BDDMockito.given(this.authoritiesService.findAuthority(InscripcionControllerTests.TEST_ORGANIZADOR_NOMBRE)).willReturn("organizador");
		}

	  @WithMockUser(username = InscripcionControllerTests.TEST_MANAGER_NOMBRE)
	  @Test 
	  void testProcessFindFormSuccessManager() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("inscripciones/inscripcionesList")); 
	  }
	  
	  @WithMockUser(username = InscripcionControllerTests.TEST_ANCIANO_NOMBRE)
	  @Test 
	  void testProcessFindFormSuccessAnciano() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("inscripciones/inscripcionesList")); 
	  }
	  
	  //un organizador no puede acceder al listado de inscripciones de una residencia
	  @WithMockUser(username = InscripcionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	  @Test 
	  void testProcessFindFormSuccessErrorOrganizador() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones"))
	  .andExpect(MockMvcResultMatchers.view().name("exception")); 
	  }
	
	  @WithMockUser(username = InscripcionControllerTests.TEST_ANCIANO_NOMBRE)
	  @Test
	  void testInitCreationFormSuccesfull() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/new/{residenciaId}", InscripcionControllerTests.TEST_INSCRIPCION_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("inscripciones/createOrUpdateInscripcionForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("inscripcion"));
	  }
	  
	  @WithMockUser(username = InscripcionControllerTests.TEST_ANCIANO_NOMBRE)
	  @Test
		void testProcessCreationFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/inscripciones/new/{residenciaId}", InscripcionControllerTests.TEST_INSCRIPCION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "dec")
						.param("estado", "aceptada")
						.param("fecha", String.valueOf(this.hoy)))
						.andExpect(MockMvcResultMatchers.status().isOk());		
	  }
	  
	  //un organizador no puede enviar inscripcion de una residencia
	  @WithMockUser(username = InscripcionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	  @Test
		void testProcessCreationFormErrorOrganizador() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/inscripciones/new/{residenciaId}", InscripcionControllerTests.TEST_INSCRIPCION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "dec")
						.param("estado", "aceptada")
						.param("fecha", String.valueOf(this.hoy)))
				  .andExpect(MockMvcResultMatchers.view().name("exception")); 
		}
	
	  @WithMockUser(username = InscripcionControllerTests.TEST_MANAGER_NOMBRE)
	  @Test
	  void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/{inscripcionId}/edit", InscripcionControllerTests.TEST_INSCRIPCION_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(model().attributeExists("inscripcion"))
		.andExpect(view().name("inscripciones/createOrUpdateInscripcionForm"));
	  }

	  @WithMockUser(username = InscripcionControllerTests.TEST_MANAGER_NOMBRE)
	  @Test
		void testProcessUpdateFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/inscripciones/{inscripcionId}/edit", InscripcionControllerTests.TEST_INSCRIPCION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("estado", "aceptada")
						.param("justificacion", "si"))
						.andExpect(MockMvcResultMatchers.status().isOk());
		}
	
	  @WithMockUser(username = InscripcionControllerTests.TEST_MANAGER_NOMBRE)
		@Test
		void testShowResidencia() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/{inscripcionId}", InscripcionControllerTests.TEST_INSCRIPCION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("inscripcion"))
				.andExpect(MockMvcResultMatchers.model().attribute("inscripcion", Matchers.hasProperty("estado", Matchers.is("pendiente"))))
				.andExpect(MockMvcResultMatchers.model().attribute("inscripcion", Matchers.hasProperty("justificacion", Matchers.is("si"))))
				.andExpect(MockMvcResultMatchers.model().attribute("inscripcion", Matchers.hasProperty("declaracion", Matchers.is("dec"))))
				.andExpect(MockMvcResultMatchers.view().name("inscripciones/inscripcionesDetails"));
	}	
}