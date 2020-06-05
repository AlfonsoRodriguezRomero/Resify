package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
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
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.FeedbackService;
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

@WebMvcTest(value = ExcursionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ExcursionControllerTests {

	private static final int		TEST_EXCURSION_ID		= 4;

	private static final String		TEST_ORGANIZADOR_NOMBRE	= "organizador4";
	
	@MockBean
	private PeticionExcursionService		peticionExcursionService;

	@MockBean
	private ExcursionService		excursionService;

	@MockBean
	private OrganizadorService		organizadorService;
	
	@MockBean
	private ManagerService 			managerService;

	@MockBean
	private AuthoritiesService 		authoritiesService;
	
	@MockBean
	private AncianoService 		ancianoService;
	
	@MockBean
	private ResidenciaService 		residenciaService;
	
	@MockBean
	private FeedbackService 		feedbackService;



	@Autowired
	private MockMvc					mockMvc;

	private Excursion				exc;
	private LocalDate				diaini					= LocalDate.now().plusDays(9);
	private LocalDate				diafin   				= LocalDate.now().plusDays(10);
	private LocalTime				horini   				= LocalTime.of(9, 0);
	private LocalTime				horfin   				= LocalTime.of(20, 0);
	Organizador						org						= new Organizador();
	User							user					= new User();


	@BeforeEach
	void setup() {
		this.user.setUsername(ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE);
		this.org.setUser(this.user);
		this.exc = new Excursion();
		this.exc.setId(ExcursionControllerTests.TEST_EXCURSION_ID);
		this.exc.setDescripcion("Prueba desc");
		this.exc.setTitulo("Prueba");
		this.exc.setFechaInicio(java.sql.Date.valueOf(diaini));
		this.exc.setFechaFin(java.sql.Date.valueOf(diafin));
		this.exc.setFinalMode(true);
		this.exc.setHoraInicio(horini);
		this.exc.setHoraFin(horfin);
		this.exc.setNumeroResidencias(6);
		this.exc.setOrganizador(this.org);
		this.exc.setRatioAceptacion(1.0);
		this.excursionService.saveExcursion(this.exc);
		BDDMockito.given(this.excursionService.findExcursionById(ExcursionControllerTests.TEST_EXCURSION_ID)).willReturn(this.exc);
		BDDMockito.given(this.organizadorService.findOrganizadorByUsername(ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)).willReturn(this.org);
		BDDMockito.given(this.authoritiesService.findAuthority(ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)).willReturn("organizador");
	}

	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testProcessFindFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("excursiones/excursionesList"));
	}

	@WithMockUser(authorities = "organizador")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/new"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("excursiones/createOrUpdateExcursionForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("excursion"));
	}

	@WithMockUser(authorities = "organizador")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/new")
				.param("titulo", "Prueba")
				.param("descripcion", "Prueba descrip")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
				.param("horaInicio", "10:00")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
				.param("horaFin", "20:00")
				.param("numeroResidencias", "10")
				.param("ratioAceptacion", "1.0"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(authorities = "organizador")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/new")
				.param("titulo", "Prueba")
				.param("descripcion", "")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
				.param("horaInicio", "10:00")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
				.param("horaFin", "20:00")
				.param("numeroResidencias", "10")
				.param("ratioAceptacion", "1.0"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("excursion"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("excursiones/createOrUpdateExcursionForm"));
	}

	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testShowExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}", ExcursionControllerTests.TEST_EXCURSION_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("excursion"))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("descripcion", Matchers.is("Prueba desc"))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("titulo", Matchers.is("Prueba"))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("fechaInicio", Matchers.is(java.sql.Date.valueOf(this.diaini)))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("fechaFin", Matchers.is(java.sql.Date.valueOf(this.diafin)))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("finalMode", Matchers.is(true))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("horaInicio", Matchers.is(this.horini))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("horaFin", Matchers.is(this.horfin))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("numeroResidencias", Matchers.is(6))))
			.andExpect(MockMvcResultMatchers.model().attribute("excursion", Matchers.hasProperty("ratioAceptacion", Matchers.is(1.0))))
			.andExpect(MockMvcResultMatchers.view().name("excursiones/excursionesDetails"));
	}

	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testInitUpdateExcursionForm() throws Exception {
		mockMvc.perform(get("/excursiones/{excursionId}/edit", ExcursionControllerTests.TEST_EXCURSION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(model().attributeExists("excursion"))
				.andExpect(model().attribute("excursion", hasProperty("descripcion", is("Prueba desc"))))
				.andExpect(model().attribute("excursion", hasProperty("titulo", is("Prueba"))))
				.andExpect(model().attribute("excursion", hasProperty("fechaInicio", is(java.sql.Date.valueOf(this.diaini)))))
				.andExpect(model().attribute("excursion", hasProperty("fechaFin", is(java.sql.Date.valueOf(this.diafin)))))
				.andExpect(model().attribute("excursion", hasProperty("finalMode", is(true))))
				.andExpect(model().attribute("excursion", hasProperty("horaInicio", is(this.horini))))
				.andExpect(model().attribute("excursion", hasProperty("horaFin", is(this.horfin))))
				.andExpect(model().attribute("excursion", hasProperty("numeroResidencias", is(6))))
				.andExpect(model().attribute("excursion", hasProperty("ratioAceptacion", is(1.0))))				
				.andExpect(view().name("excursiones/createOrUpdateExcursionForm"));
	}
	
	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testProcessUpdateFormFinalModeTrue() throws Exception {
		this.exc.setFinalMode(true);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/edit",ExcursionControllerTests.TEST_EXCURSION_ID)
				.param("finalMode", "true")
				.param("titulo", "Prueba")
				.param("descripcion", "Prueba descrip")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
				.param("horaInicio", "10:00")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
				.param("horaFin", "20:00")
				.param("numeroResidencias", "10")
				.param("ratioAceptacion", "1.0"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.exc.setFinalMode(false);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/edit",ExcursionControllerTests.TEST_EXCURSION_ID)
				.param("titulo", "Prueba2")
				.param("descripcion", "Prueba descrip")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
				.param("horaInicio", "10:00")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
				.param("horaFin", "20:00")
				.param("numeroResidencias", "10")
				.param("ratioAceptacion", "1.0")
				.param("finalMode", "true"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.exc.setFinalMode(false);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/edit",ExcursionControllerTests.TEST_EXCURSION_ID)
				.param("titulo", "Prueba")
				.param("descripcion", "")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
				.param("horaInicio", "10:00")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
				.param("horaFin", "20:00")
				.param("numeroResidencias", "10")
				.param("ratioAceptacion", "1.0"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("excursion"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("excursiones/createOrUpdateExcursionForm"));
	}
	
	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testProcessDeleteSuccess() throws Exception {
		this.exc.setFinalMode(false);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/delete",ExcursionControllerTests.TEST_EXCURSION_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(username = ExcursionControllerTests.TEST_ORGANIZADOR_NOMBRE)
	@Test
	void testProcessDeleteFinalModeTrue() throws Exception {
		this.exc.setFinalMode(true);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/delete",ExcursionControllerTests.TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	//no debe poder acceder a la edición de una excursión un organizador distinto a su creador
	@WithMockUser(username = "organizador1")
	@Test
	void testInitUpdateConOrganizadorIncorrecto() throws Exception {
		mockMvc.perform(get("/excursiones/{excursionId}/edit", ExcursionControllerTests.TEST_EXCURSION_ID))			
				.andExpect(view().name("exception"));
	}

	//no debe poder editar una excursión un organizador distinto al que la creó
	@WithMockUser(username = "organizador1")
	@Test
	void testProcessUpdateConOrganizadorIncorrecto() throws Exception {
		this.exc.setFinalMode(false);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/edit",ExcursionControllerTests.TEST_EXCURSION_ID)
				.param("titulo", "Prueba2")
				.param("descripcion", "Prueba descrip")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
				.param("horaInicio", "10:00")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
				.param("horaFin", "20:00")
				.param("numeroResidencias", "10")
				.param("ratioAceptacion", "1.0")
				.param("finalMode", "true"))
		.andExpect(view().name("exception"));
	}
	
	//no debe acceder al show de una excursión siendo un organizador distinto a su creador
	@WithMockUser(username = "organizador1", authorities = "organizador")
	@Test
	void testShowConOrganizadorEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}", ExcursionControllerTests.TEST_EXCURSION_ID)).andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	//no debe acceder al show de una excursión siendo anciano si no es de su residencia
	@WithMockUser(username = "anciano3", authorities = "anciano")
	@Test
	void testShowConAncianoEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}", ExcursionControllerTests.TEST_EXCURSION_ID)).andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	//no debe poder borrar una excursión siendo manager si no es suya
	@WithMockUser(username = "organizador1", authorities = "organizador")
	@Test
	void testProcessDeleteConManagerEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/delete", ExcursionControllerTests.TEST_EXCURSION_ID))
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	


}