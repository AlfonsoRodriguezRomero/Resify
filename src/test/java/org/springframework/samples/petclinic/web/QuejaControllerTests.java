
package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.Instant;
import java.util.ArrayList;
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
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Queja;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.QuejaService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = QuejaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class QuejaControllerTests {

	private static final int TEST_QUEJA_ID = 1;

	private static final String TEST_MANAGER_NOMBRE = "manager";

	private static final String TEST_ANCIANO_NOMBRE = "anciano";

	@MockBean
	private QuejaService quejaService;

	@MockBean
	private ManagerService managerService;

	@MockBean
	private AncianoService ancianoService;

	@MockBean
	private ResidenciaService residenciaService;

	@Autowired
	private MockMvc mockMvc;

	private Queja q;
	private Date fecha = Date.from(Instant.now().minusMillis(1));
	private Residencia res = new Residencia();
	Anciano anc = new Anciano();
	Manager man = new Manager();
	User userMan = new User();
	User uesrAnc = new User();

	private Iterable<Queja> quejas = new ArrayList<>();

	@BeforeEach
	void setup() {
		this.userMan.setUsername(QuejaControllerTests.TEST_MANAGER_NOMBRE);
		this.uesrAnc.setUsername(QuejaControllerTests.TEST_ANCIANO_NOMBRE);
		this.man.setUser(this.userMan);
		this.anc.setUser(this.uesrAnc);
		this.anc.setNombre("Pepe");
		this.anc.setApellidos("Sol");

		this.q = new Queja();
		this.q.setId(QuejaControllerTests.TEST_QUEJA_ID);
		this.q.setTitulo("Prueba");
		this.q.setDescripcion("Prueba desc");
		this.q.setFecha(fecha);
		this.q.setAnonimo(false);
		this.q.setAnciano(this.anc);
		this.quejaService.saveQueja(this.q);

		ArrayList<Queja> arrayQuejas = new ArrayList<>();
		arrayQuejas.add(q);
		this.quejas = arrayQuejas;

		BDDMockito.given(this.quejaService.findQuejaById(QuejaControllerTests.TEST_QUEJA_ID)).willReturn(this.q);

		BDDMockito.given(this.ancianoService.findAncianoByUsername(QuejaControllerTests.TEST_ANCIANO_NOMBRE))
				.willReturn(this.anc);

		BDDMockito.given(this.managerService.findManagerByUsername(QuejaControllerTests.TEST_MANAGER_NOMBRE))
				.willReturn(this.man);

		BDDMockito.given(this.residenciaService.findResidenciaByAnciano(this.anc)).willReturn(this.res);

		BDDMockito.given(this.quejaService.findQuejasByManager(this.man)).willReturn(this.quejas);

	}

	@WithMockUser(username = QuejaControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("quejas/quejasList"));
	}

	@WithMockUser(authorities = "anciano")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("quejas/createOrUpdateQuejaForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("queja"));
	}

	// No debe poder acceder a la creacion de una queja si en ese dia ya ha
	// realizado 3 o más
	@WithMockUser(username = QuejaControllerTests.TEST_ANCIANO_NOMBRE)
	@Test
	void testProcessCreationFormHasErrorsMaxNumeroPorDia() throws Exception {
		BDDMockito.given(this.quejaService.countQuejasHoyByAnciano(this.anc)).willReturn(3.);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("quejas/createOrUpdateQuejaForm"))
				.andExpect(MockMvcResultMatchers.model().attribute("hasMaxQuejas", true));
	}

	// No debe poder acceder a la creacion de una queja si no ha sido aceptado en
	// ninguna residencia
	@WithMockUser(username = QuejaControllerTests.TEST_ANCIANO_NOMBRE)
	@Test
	void testProcessCreationFormHasErrorsNoResidencia() throws Exception {
		BDDMockito.given(this.residenciaService.findResidenciaByAnciano(this.anc)).willReturn(null);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("quejas/createOrUpdateQuejaForm"))
				.andExpect(MockMvcResultMatchers.model().attribute("hasResidencia", false));
	}

	@WithMockUser(username = QuejaControllerTests.TEST_ANCIANO_NOMBRE)
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/quejas/new").with(csrf()).param("titulo", "Prueba")
						.param("descripcion", "Prueba descrip").param("anonimo", "true"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(authorities = "anciano")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/quejas/new").with(csrf()).param("titulo", "")
						.param("descripcion", "").param("anonimo", "false"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("queja", "titulo", "descripcion"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("quejas/createOrUpdateQuejaForm"));
	}

	// No debe poder crear una queja si en ese dia ya ha realizado 3 o mas
	@WithMockUser(username = QuejaControllerTests.TEST_ANCIANO_NOMBRE)
	@Test
	void testProcessCreationFormErrorMaxNumeroPorDia() throws Exception {
		BDDMockito.given(this.quejaService.countQuejasHoyByAnciano(this.anc)).willReturn(3.);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/quejas/new").with(csrf()).param("titulo", "Prueba")
						.param("descripcion", "Prueba descrip").param("anonimo", "true"))
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// No debe poder crear una queja si no ha sido aceptado en ninguna residencia
	@WithMockUser(username = QuejaControllerTests.TEST_ANCIANO_NOMBRE)
	@Test
	void testProcessCreationFormErrorNoResidencia() throws Exception {
		BDDMockito.given(this.residenciaService.findResidenciaByAnciano(this.anc)).willReturn(null);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/quejas/new").with(csrf()).param("titulo", "Prueba")
						.param("descripcion", "Prueba descrip").param("anonimo", "true"))
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = QuejaControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testShowQueja() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/{quejaId}", QuejaControllerTests.TEST_QUEJA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("queja"))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("descripcion", Matchers.is("Prueba desc"))))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("titulo", Matchers.is("Prueba"))))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("fecha", Matchers.is(this.fecha))))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("anonimo", Matchers.is(false))))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("anciano", Matchers.hasProperty("nombre", Matchers.is("Pepe")))))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("anciano", Matchers.hasProperty("apellidos", Matchers.is("Sol")))))
				.andExpect(MockMvcResultMatchers.view().name("quejas/quejasDetails"));
	}

	// Un manager no puede acceder a la vista de una queja que no es suya
	@WithMockUser(username = "manager1")
	@Test
	void testShowQuejaErrorManagerEquivocado() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/{quejaId}", QuejaControllerTests.TEST_QUEJA_ID))
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}