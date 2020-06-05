
package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.VisitaSanitaria;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.service.VisitaSanitariaService;
import org.springframework.samples.petclinic.web.VisitaSanitariaController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*
 * @TestPropertySource(
 * locations = "classpath:application-mysql.properties")
 */
public class VisitaSanitariaControllerE2ETest {

	private static final int TEST_VS_ID = 1;

	private LocalTime horini = LocalTime.of(9, 0);
	private LocalTime horfin = LocalTime.of(20, 0);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private VisitaSanitariaService visitaSanitariaService;

	@Autowired
	private AncianoService ancianoService;

	@Autowired
	private ResidenciaService residenciaService;

	@Autowired
	private ManagerService managerService;

	
	@WithMockUser(username = "manager5", authorities = { "manager" })
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/visitasSanitariasList"));
	}

	// No debe poder acceder al listado como anciano
	@WithMockUser(username = "anciano1")
	@Test
	void testProcessFindFormErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// No debe poder acceder al listado como organizador
	@WithMockUser(username = "organizador1")
	@Test
	void testProcessFindFormErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = "manager5", authorities = { "manager" })
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/createOrUpdateVisitaSanitariaForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("visitaSanitaria"));
	}

	@WithMockUser(username = "manager5", authorities = { "manager" })
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "Motivo prueba")
						.param("descripcion", "Prueba descrip").param("sanitario", "sanitario prueba")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01")
						.param("horaInicio", String.valueOf(this.horini)).param("horaFin", String.valueOf(this.horfin))
						.param("anciano.id", String.valueOf(22)))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "manager5", authorities = { "manager" })
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	@Transactional
	void testProcessCreationFormHasErrorsBlank() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "")
						.param("sanitario", "").param("descripcion", "Prueba descrip")
						.param("horaInicio", String.valueOf(this.horini)).param("horaFin", String.valueOf(this.horfin))
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01")
						.param("anciano.id", String.valueOf(22)))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("visitaSanitaria"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/createOrUpdateVisitaSanitariaForm"));
	}

	@WithMockUser(username = "manager5", authorities = { "manager" })
	@Test
	void testProcessCreationFormHasErrorsAncianoSinDependencia() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/visitas-sanitarias/new").param("motivo", "Motivo prueba")
						.param("sanitario", "sanitario prueba").param("descripcion", "Prueba descrip")
						.param("horaInicio", "10:00").param("horaFin", "12:00")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01")
						.param("anciano.id", "23"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// No debe poder crear visita sanitaria siendo anciano
	@WithMockUser(username = "anciano1")
	@Test
	void testInitCreationFormErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/new"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// No debe poder crear visita sanitaria siendo organizador
	@WithMockUser(username = "organizador1")
	@Test
	void testInitCreationFormErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/new"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = "manager5", authorities = { "manager" })

	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	@Transactional
	void testShowVisitaSanitaria() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/{visitaSanitariaId}", TEST_VS_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("visitaSanitaria"))
				.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
						Matchers.hasProperty("descripcion", Matchers.is("visita sanitaria"))))
				.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
						Matchers.hasProperty("motivo", Matchers.is("Ejemplo"))))
				.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
						Matchers.hasProperty("sanitario", Matchers.is("Sanitario prueba"))))
				.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
						Matchers.hasProperty("horaInicio", Matchers.is(LocalTime.of(17, 00)))))
				.andExpect(MockMvcResultMatchers.model().attribute("visitaSanitaria",
						Matchers.hasProperty("horaFin", Matchers.is(LocalTime.of(22, 00)))))
				.andExpect(MockMvcResultMatchers.view().name("visitasSanitarias/visitasSanitariasDetails"));
	}

// No debe poder ver una visita sanitaria siendo anciano
	@WithMockUser(username = "anciano1")
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	@Transactional
	void testShowVisitaSanitariaErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/{visitaSanitariaId}", TEST_VS_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// No debe poder ver una visita sanitaria siendo organizador
	@WithMockUser(username = "organizador1")
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	@Transactional
	void testShowVisitaSanitariaErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visitas-sanitarias/{visitaSanitariaId}", TEST_VS_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}


}
