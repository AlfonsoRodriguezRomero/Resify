
package org.springframework.samples.petclinic.web;

import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.VisitaSanitaria;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.service.VisitaSanitariaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = VisitaSanitariaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VisitaSanitariaControllerTests {

	private static final int		TEST_VISITA_SANITARIA_ID	= 1;

	private static final int		TEST_ANCIANO_ID				= 1;

	private static final String		TEST_MANAGER_NOMBRE			= "manager";

	@MockBean
	private VisitaSanitariaService	visitaSanitariaService;

	@MockBean
	private ManagerService			managerService;

	@MockBean
	private AncianoService			ancianoService;

	@MockBean
	private AuthoritiesService		authoritiesService;

	@MockBean
	private ResidenciaService		residenciaService;

	@Autowired
	private MockMvc					mockMvc;

	private VisitaSanitaria			vis;
	private Date					diaini						= Date.from(Instant.now().minusSeconds(1000));
	private LocalTime				horini						= LocalTime.of(9, 0);
	private LocalTime				horfin						= LocalTime.of(20, 0);
	private Anciano					anciano						= new Anciano();
	private Residencia				resi						= new Residencia();
	private List<Anciano>			misAncianos					= new ArrayList<>();
	Manager							man							= new Manager();
	User							user						= new User();


	@BeforeEach
	void setup() {
		this.anciano.setTieneDependenciaGrave(true);
		this.user.setUsername(VisitaSanitariaControllerTests.TEST_MANAGER_NOMBRE);
		this.man.setUser(this.user);
		this.resi.setManager(this.man);
		this.anciano.setId(VisitaSanitariaControllerTests.TEST_ANCIANO_ID);
		this.misAncianos.add(this.anciano);
		this.vis = new VisitaSanitaria();
		this.vis.setId(VisitaSanitariaControllerTests.TEST_VISITA_SANITARIA_ID);
		this.vis.setMotivo("Motivo prueba");
		this.vis.setSanitario("sanitario prueba");
		this.vis.setDescripcion("Prueba desc");
		this.vis.setFecha(this.diaini);
		this.vis.setHoraInicio(this.horini);
		this.vis.setHoraFin(this.horfin);
		this.vis.setResidencia(this.resi);
		this.vis.setAnciano(this.anciano);
		this.visitaSanitariaService.saveVisitaSanitaria(this.vis);
		BDDMockito.given(this.visitaSanitariaService.findVisitaSanitariaById(VisitaSanitariaControllerTests.TEST_VISITA_SANITARIA_ID)).willReturn(this.vis);
		BDDMockito.given(this.managerService.findManagerByUsername(VisitaSanitariaControllerTests.TEST_MANAGER_NOMBRE)).willReturn(this.man);
		BDDMockito.given(this.ancianoService.findAncianoById(VisitaSanitariaControllerTests.TEST_ANCIANO_ID)).willReturn(this.anciano);
		BDDMockito.given(this.residenciaService.findMine(this.man)).willReturn(this.resi);
		BDDMockito.given(this.ancianoService.findAncianosMiResidencia(this.resi))
				.willReturn(this.misAncianos);
	}

	@WithMockUser(username = VisitaSanitariaControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/visitasSanitariasList"));
	}

	// No debe poder acceder al listado si no tiene registrado una residencia, le
	// manda a la vista de creación de una
	@WithMockUser(username = "manager1")
	@Test
	void testProcessFindFormErrorNoResidencia() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:residencias/new"));
	}

	@WithMockUser(username = VisitaSanitariaControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/createOrUpdateVisitaSanitariaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("visitaSanitaria"));
	}

	@WithMockUser(username = VisitaSanitariaControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	void testProcessCreationFormSuccess() throws Exception {
		this.anciano.setTieneDependenciaGrave(true);
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "Motivo prueba").param("descripcion", "Prueba descrip").param("sanitario", "sanitario prueba").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("fecha", "2020/01/01").param("horaInicio", String.valueOf(this.horini)).param("horaFin", String.valueOf(this.horfin)).param("anciano.id", String.valueOf(VisitaSanitariaControllerTests.TEST_ANCIANO_ID)))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = VisitaSanitariaControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testProcessCreationFormHasErrorsBlank() throws Exception {
		this.anciano.setTieneDependenciaGrave(true);
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "").param("sanitario", "").param("descripcion", "Prueba descrip").param("horaInicio", String.valueOf(this.horini)).param("horaFin", String.valueOf(this.horfin))
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01").param("anciano.id", String.valueOf(VisitaSanitariaControllerTests.TEST_ANCIANO_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("visitaSanitaria")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/createOrUpdateVisitaSanitariaForm"));
	}

	@WithMockUser(username = VisitaSanitariaControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testProcessCreationFormHasErrorsAncianoSinDependencia() throws Exception {
		this.anciano.setTieneDependenciaGrave(false);
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "Motivo prueba").param("sanitario", "sanitario prueba").param("descripcion", "Prueba descrip").param("horaInicio", String.valueOf(this.horini))
				.param("horaFin", String.valueOf(this.horfin)).with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01").param("anciano.id", String.valueOf(VisitaSanitariaControllerTests.TEST_ANCIANO_ID)))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "manager1")
	@Test
	void testProcessCreationFormHasErrorsConManagerEquivocado() throws Exception {
		this.anciano.setTieneDependenciaGrave(true);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "Motivo prueba")
						.param("descripcion", "Prueba descrip").param("sanitario", "sanitario prueba")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01")
						.param("horaInicio", String.valueOf(this.horini)).param("horaFin", String.valueOf(this.horfin))
						.param("anciano.id", String.valueOf(TEST_ANCIANO_ID)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = VisitaSanitariaControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testShowVisitaSanitaria() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/{visitaSanitariaId}", VisitaSanitariaControllerTests.TEST_VISITA_SANITARIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("visitaSanitaria")).andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria", Matchers.hasProperty("descripcion", Matchers.is("Prueba desc"))))
			.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria", Matchers.hasProperty("motivo", Matchers.is("Motivo prueba"))))
			.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria", Matchers.hasProperty("sanitario", Matchers.is("sanitario prueba"))))
			.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria", Matchers.hasProperty("fecha", Matchers.is(this.diaini))))
			.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria", Matchers.hasProperty("horaInicio", Matchers.is(this.horini))))
			.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria", Matchers.hasProperty("horaFin", Matchers.is(this.horfin)))).andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/visitasSanitariasDetails"));
	}
	@WithMockUser(username = VisitaSanitariaControllerTests.TEST_MANAGER_NOMBRE)
	@Test
	void testProcessDeleteSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/{visitaSanitariaId}/delete",
						VisitaSanitariaControllerTests.TEST_VISITA_SANITARIA_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/visitas-sanitarias"));
	}

	
	//No debe poder borrar una visita si no es de su residencia
	@WithMockUser(username = "manager1")
	@Test
	void testShowVisitaSanitariaConManagerEquivocado() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/{visitaSanitariaId}",
						VisitaSanitariaControllerTests.TEST_VISITA_SANITARIA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
