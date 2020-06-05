
package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalTime;

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
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = ResidenciaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ResidenciaControllerTest {

	private static final int		TEST_RESIDENCIA_ID	= 1;

	private static final String		TEST_MANAGER_NOMBRE		= "manager";
	private static final String		TEST_ORGANIZADOR_NOMBRE		= "organizador";
	private static final String		TEST_ANCIANO_NOMBRE		= "anciano";


	@MockBean
	private ResidenciaService		residenciaService;

	@MockBean
	private ManagerService			managerService;

	@MockBean
	private AncianoService		ancianoService;

	@MockBean
	private InscripcionService	inscripcionService;

	@MockBean
	private OrganizadorService	organizadorService;
	
	@MockBean
	private AuthoritiesService			authoritiesService;

	@Autowired
	private MockMvc					mockMvc;

	private Residencia				resi;
	Manager							man						= new Manager();
	Organizador 					organ 					= new Organizador();
	Anciano 						anc 					= new Anciano();
	User							user					= new User();
	User							userOrganiza			= new User();
	User							userAnciano				= new User();
	User							userMan2					= new User();
	Manager							man2						= new Manager();
	
	private LocalTime horini = LocalTime.of(9, 0);
	private LocalTime horfin = LocalTime.of(20, 0);

	@BeforeEach
	void setup() {
		this.user.setUsername(ResidenciaControllerTest.TEST_MANAGER_NOMBRE);
		this.userOrganiza.setUsername(ResidenciaControllerTest.TEST_ORGANIZADOR_NOMBRE);
		this.userAnciano.setUsername(ResidenciaControllerTest.TEST_ANCIANO_NOMBRE);
		this.userMan2.setUsername("managerIncorrecto");
		
		this.man.setUser(this.user);
		this.anc.setUser(this.userAnciano);
		this.organ.setUser(this.userOrganiza);
		this.man2.setUser(this.userMan2);
		
		this.resi = new Residencia();
		this.resi.setId(ResidenciaControllerTest.TEST_RESIDENCIA_ID);
		LocalTime horaApertura = LocalTime.of(07, 00);
		LocalTime horaCierre = LocalTime.of(21, 00);
		this.resi.setManager(this.man);
		this.resi.setAceptaDependenciaGrave(false);
		this.resi.setAforo(100);
		this.resi.setCorreo("residencia1@mail.es");
		this.resi.setDescripcion("Descripcion de prueba");
		this.resi.setDireccion("Direccion");
		this.resi.setEdadMaxima(76);
		this.resi.setHoraApertura(horaApertura);
		this.resi.setHoraCierre(horaCierre);
		this.resi.setMasInfo("http://www.resi1.com");
		this.resi.setNombre("Reidencia 1");
		this.resi.setTelefono("987654321");
		this.residenciaService.saveResidencia(this.resi);
		BDDMockito.given(this.residenciaService.findResidenciaById(ResidenciaControllerTest.TEST_RESIDENCIA_ID)).willReturn(this.resi);
		BDDMockito.given(this.managerService.findManagerByUsername(ResidenciaControllerTest.TEST_MANAGER_NOMBRE)).willReturn(this.man);
		BDDMockito.given(this.ancianoService.findAncianoByUsername(ResidenciaControllerTest.TEST_ANCIANO_NOMBRE)).willReturn(this.anc);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername(ResidenciaControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn(this.organ);
		BDDMockito.given(this.managerService.findManagerByUsername("managerIncorrecto")).willReturn(this.man2);

		BDDMockito.given(this.authoritiesService.findAuthority(ResidenciaControllerTest.TEST_MANAGER_NOMBRE)).willReturn("manager");
		BDDMockito.given(this.authoritiesService.findAuthority(ResidenciaControllerTest.TEST_ANCIANO_NOMBRE)).willReturn("anciano");
		BDDMockito.given(this.authoritiesService.findAuthority(ResidenciaControllerTest.TEST_ORGANIZADOR_NOMBRE)).willReturn("organizador");

	}
	
	  @WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
	  @Test 
	  void testProcessFindFormSuccess() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias"))
	  .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	  .andExpect(MockMvcResultMatchers.view().name("redirect:residencias/new")); 
	  }
	  
	  @WithMockUser(username = ResidenciaControllerTest.TEST_ORGANIZADOR_NOMBRE)
	  @Test 
	  void testProcessFindFormSuccessOrganizador() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("residencias/residenciasList")); 
	  }
	  
	  @WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
	  @Test 
	  void testProcessFindTopFormSuccess() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/top"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("residencias/residenciasList")); 
	  }
	  
	  @WithMockUser(username = ResidenciaControllerTest.TEST_ORGANIZADOR_NOMBRE)
	  @Test 
	  void testProcessFindRatioFormSuccess() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/ratio"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("residencias/residenciasListRatio")); 
	  }
	  
	  @WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
	  @Test 
	  void testProcessFindNoParticipantesFormSuccess() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/no-participantes"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("residencias/residenciasList")); 
	  }
	  
	  @WithMockUser(authorities = "manager")
		@Test
		void testInitCreationForm() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/new"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("residencias/createOrUpdateResidenciaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("residencia"));
		}
	  
		@WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
		@Test
		void testProcessCreationFormSuccess() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.post("/residencias/new").with(csrf())
					.param("nombre", "residee")
					.param("direccion", "direc")
					.param("descripcion", "descp")
					.param("aforo", "100")
					.param("masInfo", "")
					.param("telefono", "674567123")
					.param("correo", "resi@gmail.com")
					.param("horaApertura", String.valueOf(this.horini))
					.param("horaCierre", String.valueOf(this.horfin))
					.param("edadMaxima", "82")
					.param("aceptaDependenciaGrave", "true"))
					.andExpect(MockMvcResultMatchers.status().is3xxRedirection());			
			}
		
		@WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
		@Test
		void testProcessCreationFormHasErrors() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.post("/residencias/new").with(csrf())
					.param("nombre", "residee")
					.param("direccion", "direc")
					.param("descripcion", "")
					.param("aforo", "100")
					.param("masInfo", "")
					.param("telefono", "674567123")
					.param("correo", "resi@gmail.com")
					.param("horaApertura", String.valueOf(this.horini))
					.param("horaCierre", String.valueOf(this.horfin))
					.param("edadMaxima", "82")
					.param("aceptaDependenciaGrave", "true"))
					.andExpect(MockMvcResultMatchers.model().attributeHasErrors("residencia"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("residencias/createOrUpdateResidenciaForm"));
			}
		
		@WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
		@Test
		void testShowResidencia() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/{residenciaId}", ResidenciaControllerTest.TEST_RESIDENCIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("residencia"))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("nombre", Matchers.is("Reidencia 1"))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("direccion", Matchers.is("Direccion"))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("descripcion", Matchers.is("Descripcion de prueba"))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("aforo", Matchers.is(100))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("masInfo", Matchers.is("http://www.resi1.com"))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("telefono", Matchers.is("987654321"))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("correo", Matchers.is("residencia1@mail.es"))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("horaCierre", Matchers.is(LocalTime.of(21, 00)))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("horaApertura", Matchers.is(LocalTime.of(07, 00)))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("edadMaxima", Matchers.is(76))))
				.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("aceptaDependenciaGrave", Matchers.is(false))))	
				.andExpect(MockMvcResultMatchers.view().name("residencias/residenciasDetails"));
		}
		
		@WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
		@Test
		void testInitUpdateExcursionForm() throws Exception {
			mockMvc.perform(get("/residencias/{residenciaId}/edit", ResidenciaControllerTest.TEST_RESIDENCIA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(model().attributeExists("residencia"))
				.andExpect(model().attribute("residencia", hasProperty("nombre", is("Reidencia 1"))))
				.andExpect(model().attribute("residencia", hasProperty("direccion", is("Direccion"))))
				.andExpect(model().attribute("residencia", hasProperty("descripcion", is("Descripcion de prueba"))))
				.andExpect(model().attribute("residencia", hasProperty("aforo", is(100))))
				.andExpect(model().attribute("residencia", hasProperty("masInfo", is("http://www.resi1.com"))))
				.andExpect(model().attribute("residencia", hasProperty("telefono", is("987654321"))))
				.andExpect(model().attribute("residencia", hasProperty("correo", is("residencia1@mail.es"))))
				.andExpect(model().attribute("residencia", hasProperty("horaCierre", is(LocalTime.of(21, 00)))))
				.andExpect(model().attribute("residencia", hasProperty("horaApertura", is(LocalTime.of(07, 00)))))	
				.andExpect(model().attribute("residencia", hasProperty("edadMaxima", is(76))))
				.andExpect(model().attribute("residencia", hasProperty("aceptaDependenciaGrave", is(false))))
				.andExpect(view().name("residencias/createOrUpdateResidenciaForm"));
		}
		
		@WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
		@Test
		void testProcessUpdateFormSuccess() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.post("/residencias/{residenciaId}/edit", ResidenciaControllerTest.TEST_RESIDENCIA_ID)
					.with(csrf())
					.param("nombre", "residee")
					.param("direccion", "direc")
					.param("descripcion", "desc")
					.param("aforo", "100")
					.param("masInfo", "")
					.param("telefono", "674567123")
					.param("correo", "resi@gmail.com")
					.param("horaApertura", String.valueOf(this.horini))
					.param("horaCierre", String.valueOf(this.horfin))
					.param("edadMaxima", "82")
					.param("aceptaDependenciaGrave", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
		}
		
		@WithMockUser(username = ResidenciaControllerTest.TEST_MANAGER_NOMBRE)
		@Test
		void testProcessUpdateFormHasErrors() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.post("/residencias/{residenciaId}/edit", ResidenciaControllerTest.TEST_RESIDENCIA_ID)
					.with(csrf())
					.param("nombre", "residee")
					.param("direccion", "direc")
					.param("descripcion", "")
					.param("aforo", "100")
					.param("masInfo", "")
					.param("telefono", "")
					.param("correo", "resi@gmail.com")
					.param("horaApertura", String.valueOf(this.horini))
					.param("horaCierre", String.valueOf(this.horfin))
					.param("edadMaxima", "82")
					.param("aceptaDependenciaGrave", "false"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("residencia"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("residencias/createOrUpdateResidenciaForm"));		
		}
		
		//no debe poder acceder al form de editar una residencia un manager distinto al que la creó
		@WithMockUser(username = "managerIncorrecto")
		 @Test 
		 void testInitUpdateResidenciaManagerEquivocado() throws Exception {
				mockMvc.perform(get("/residencias/{residenciaId}/edit", ResidenciaControllerTest.TEST_RESIDENCIA_ID))
					.andExpect(MockMvcResultMatchers.view().name("exception")); 
		 }
		
		//no debe poder editar una residencia un manager distinto al que la creó
		@WithMockUser(username = "managerIncorrecto")
		 @Test 
		 void testProcessUpdateResidenciaManagerEquivocado() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.post("/residencias/{residenciaId}/edit", ResidenciaControllerTest.TEST_RESIDENCIA_ID)
				.with(csrf())
				.param("nombre", "residee")
				.param("direccion", "direc")
				.param("descripcion", "desc")
				.param("aforo", "100")
				.param("masInfo", "")
				.param("telefono", "674567123")
				.param("correo", "resi@gmail.com")
				.param("horaApertura", String.valueOf(this.horini))
				.param("horaCierre", String.valueOf(this.horfin))
				.param("edadMaxima", "82")
				.param("aceptaDependenciaGrave", "true"))
			.andExpect(MockMvcResultMatchers.view().name("exception"));
		 }
		
		//no debe mostrar la residencia a un manager distinto al que la creó
		@WithMockUser(username = "managerIncorrecto")
		@Test
		void testShowResidenciaManagerEquivocado() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/{residenciaId}", ResidenciaControllerTest.TEST_RESIDENCIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
}
