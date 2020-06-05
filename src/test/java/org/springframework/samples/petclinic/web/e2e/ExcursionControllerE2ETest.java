
package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ExcursionControllerE2ETest {

	private static final int TEST_E_ID = 2;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "organizador1", authorities = { "organizador" })
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("excursiones/excursionesList"));
	}

	@WithMockUser(username = "organizador1", authorities = { "organizador" })
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("excursiones/createOrUpdateExcursionForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("excursion"));
	}

	@WithMockUser(username = "organizador1", authorities = { "organizador" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/new").param("titulo", "Prueba")
						.param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("fechaInicio", "2030/01/01").param("horaInicio", "10:00")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
						.param("horaFin", "20:00").param("numeroResidencias", "10").param("ratioAceptacion", "1.0"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "organizador1", authorities = { "organizador" })
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/new").param("titulo", "Prueba")
						.param("descripcion", "").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("fechaInicio", "2030/01/01").param("horaInicio", "10:00")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
						.param("horaFin", "20:00").param("numeroResidencias", "10").param("ratioAceptacion", "1.0"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("excursion"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("excursiones/createOrUpdateExcursionForm"));
	}

	@WithMockUser(username = "organizador1", authorities = { "organizador" })
	@Test
	void testShowExcursion() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}", ExcursionControllerE2ETest.TEST_E_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("excursion"))
				.andExpect(MockMvcResultMatchers.model().attribute("excursion",
						Matchers.hasProperty("descripcion", Matchers.is("Descripcion de prueba2"))))
				.andExpect(MockMvcResultMatchers.model().attribute("excursion",
						Matchers.hasProperty("titulo", Matchers.is("Prueba2"))))
				.andExpect(MockMvcResultMatchers.view().name("excursiones/excursionesDetails"));
	}

	@WithMockUser(username = "organizador1", authorities = { "organizador" })
	@Test
	void testInitUpdateExcursionForm() throws Exception {
		mockMvc.perform(get("/excursiones/{excursionId}/edit", TEST_E_ID))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(model().attributeExists("excursion"))
				.andExpect(model().attribute("excursion", hasProperty("descripcion", is("Descripcion de prueba2"))))
				.andExpect(model().attribute("excursion", hasProperty("titulo", is("Prueba2"))))
				.andExpect(model().attribute("excursion", hasProperty("finalMode", is(false))))
				.andExpect(model().attribute("excursion", hasProperty("horaInicio", is(LocalTime.of(17, 0)))))
				.andExpect(model().attribute("excursion", hasProperty("horaFin", is(LocalTime.of(22, 30)))))
				.andExpect(model().attribute("excursion", hasProperty("numeroResidencias", is(7))))
				.andExpect(model().attribute("excursion", hasProperty("ratioAceptacion", is(1.0))))
				.andExpect(view().name("excursiones/createOrUpdateExcursionForm"));
	}

	@WithMockUser(username = "organizador1", authorities = { "organizador" })
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/edit", TEST_E_ID)
						.param("titulo", "Prueba2").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("fechaInicio", "2030/01/01").param("descripcion", "Prueba descrip")
						.param("horaInicio", "10:00").param("horaFin", "20:00")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
						.param("numeroResidencias", "10").param("ratioAceptacion", "1.0").param("finalMode", "true"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "organizador1", authorities = { "organizador" })
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/edit", TEST_E_ID)
				.param("titulo", "Prueba").param("descripcion", "").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("fechaInicio", "2030/01/01").param("horaInicio", "10:00")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaFin", "2031/01/01")
				.param("horaFin", "20:00").param("numeroResidencias", "10").param("ratioAceptacion", "1.0"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("excursion"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("excursiones/createOrUpdateExcursionForm"));
	}
	
	//los organizadores no deben poder acceder a la lista de excursiones para feedback
	@WithMockUser(username = "organizador1")
	@Test
	void testListaExcursionParaFeedbackComoOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/feedback"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los ancianos no deben poder acceder a la lista de excursiones para feedback
	@WithMockUser(username = "anciano1")
	@Test
	void testListaExcursionParaFeedbackComoAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/feedback"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los manager no deben poder acceder a la creación de excursiones
	@WithMockUser(username = "manager1")
	@Test
	void testCreaExcursionComoOrganizador() throws Exception {
		this.mockMvc.perform(get("/excursiones/new", TEST_E_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los ancianos no deben poder acceder a la creación de excursiones
	@WithMockUser(username = "anciano1")
	@Test
	void testCreaExcursionComoAnciano() throws Exception {
		this.mockMvc.perform(get("/excursiones/new", TEST_E_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los ancianos no deben poder acceder a la edición de excursiones
	@WithMockUser(username = "anciano1")
	@Test
	void testEditaExcursionComoAnciano() throws Exception {
		this.mockMvc.perform(get("/excursiones/{excursionId}/edit", TEST_E_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los manager no deben poder acceder a la edición de excursiones
	@WithMockUser(username = "manager1")
	@Test
	void testEditaExcursionComoManager() throws Exception {
		this.mockMvc.perform(get("/excursiones/{excursionId}/edit", TEST_E_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los ancianos no deben poder borrar excursiones
	@WithMockUser(username = "anciano1")
	@Test
	void testBorraExcursionComoAnciano() throws Exception {
		this.mockMvc.perform(get("/excursiones/{excursionId}/delete", TEST_E_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los manager no deben poder borrar excursiones
	@WithMockUser(username = "manager1")
	@Test
	void testBorraExcursionComoManager() throws Exception {
		this.mockMvc.perform(get("/excursiones/{excursionId}/delete", TEST_E_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
		

}
