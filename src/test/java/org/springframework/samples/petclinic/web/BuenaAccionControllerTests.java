
package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.model.BuenaAccion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.BuenaAccionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = BuenaAccionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class BuenaAccionControllerTests {

	private static final int TEST_MANAGER_ID = 1;

	private static final int TEST_BUENA_ACCION_ID = 1;

	private static final String TEST_MANAGER_NOMBRE = "manager";

	@Autowired
	private BuenaAccionController buenaAccionController;

	@MockBean
	private BuenaAccionService buenaAccionService;

	@MockBean
	private ManagerService managerService;

	@MockBean
	private ResidenciaService residenciaService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@Autowired
	private MockMvc mockMvc;

	private BuenaAccion ba;
	private Date hoy = new Date(System.currentTimeMillis() - 1);
	private Residencia resi = new Residencia();
	Manager man = new Manager();
	User user = new User();

	@BeforeEach
	void setup() {
		this.user.setUsername(BuenaAccionControllerTests.TEST_MANAGER_NOMBRE);
		this.man.setUser(this.user);
		this.resi.setManager(this.man);
		this.ba = new BuenaAccion();
		this.ba.setId(BuenaAccionControllerTests.TEST_BUENA_ACCION_ID);
		this.ba.setTitulo("Prueba");
		this.ba.setDescripcion("Prueba desc");
		this.ba.setFecha(this.hoy);
		this.ba.setResidencia(this.resi);
		this.buenaAccionService.saveBuenaAccion(this.ba);
		BDDMockito.given(this.buenaAccionService.findBuenaAccionById(BuenaAccionControllerTests.TEST_BUENA_ACCION_ID))
				.willReturn(this.ba);
		BDDMockito.given(this.managerService.findManagerByUsername(BuenaAccionControllerTests.TEST_MANAGER_NOMBRE))
				.willReturn(this.man);
		BDDMockito.given(this.authoritiesService.findAuthority(BuenaAccionControllerTests.TEST_MANAGER_NOMBRE))
				.willReturn("manager");
		BDDMockito.given(this.residenciaService.findMine(this.man)).willReturn(this.resi);
		BDDMockito
				.given(this.managerService
						.findResidenciaByManagerUsername(BuenaAccionControllerTests.TEST_MANAGER_NOMBRE))
				.willReturn(this.resi);
	}

	@WithMockUser(username = BuenaAccionControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/buenas-acciones"))
				.andExpect(MockMvcResultMatchers.model().attribute("noTieneResi", Matchers.is(false)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("buenasAcciones/buenasAccionesList"));
	}

	// Si no tiene residencia, el atributo 'noTieneResi' sera false y no le
	// aparecera un boton de creacion
	@WithMockUser(username = "manager3")
	@Test
	void testProcessFindFormErrorNoResidencia() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/buenas-acciones"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("noTieneResi", Matchers.is(true)))
				.andExpect(MockMvcResultMatchers.view().name("buenasAcciones/buenasAccionesList"));
	}

	@WithMockUser(username = BuenaAccionControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/buenas-acciones/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("buenasAcciones/createOrUpdateBuenaAccionForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("buenaAccion"));
	}
	
	// No debe poder acceder al formulario de creacion de buena accion si no tiene residencia creada
	@WithMockUser(username = "manager3")
	@Test
	void testInitCreationFormErrorNoResidencia() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/buenas-acciones/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(authorities = "manager")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/buenas-acciones/new").param("titulo", "Prueba")
				.param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("fecha", "2020/01/01")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(authorities = "manager")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/buenas-acciones/new").param("titulo", "Prueba")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01")
						.param("descripcion", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("buenaAccion"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("buenasAcciones/createOrUpdateBuenaAccionForm"));
	}

	@WithMockUser(username = BuenaAccionControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testShowBuenaAccion() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/buenas-acciones/{buenaAccionId}",
						BuenaAccionControllerTests.TEST_BUENA_ACCION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("buenaAccion"))
				.andExpect(MockMvcResultMatchers.model().attribute("buenaAccion",
						Matchers.hasProperty("descripcion", Matchers.is("Prueba desc"))))
				.andExpect(MockMvcResultMatchers.model().attribute("buenaAccion",
						Matchers.hasProperty("titulo", Matchers.is("Prueba"))))
				.andExpect(MockMvcResultMatchers.model().attribute("buenaAccion",
						Matchers.hasProperty("fecha", Matchers.is(this.hoy))))
				.andExpect(MockMvcResultMatchers.view().name("buenasAcciones/buenasAccionesDetails"));
	}

	
	@WithMockUser(username = "manager3")
	@Test
	void testShowBuenaAccionErrorManagerEquivocado() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/buenas-acciones/{buenaAccionId}",
						BuenaAccionControllerTests.TEST_BUENA_ACCION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
