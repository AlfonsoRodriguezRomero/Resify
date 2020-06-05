
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
public class ActividadControllerE2ETest {

	private static final int TEST_A_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/actividades"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("actividades/actividadesList"));
	}

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/actividades/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("actividades/createOrUpdateActividadForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("actividad"));
	}

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/actividades/new").param("titulo", "Prueba")
						.param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("fechaInicio", "2030/01/01").param("horaInicio", "10:00").param("horaFin", "20:00"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/actividades/new").param("titulo", "")
						.param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("fechaInicio", "2030/01/01").param("horaInicio", "10:00").param("horaFin", "20:00"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("actividad"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("actividades/createOrUpdateActividadForm"));
	}

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testShowActividad() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/actividades/{actividadId}", ActividadControllerE2ETest.TEST_A_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("actividad"))
				.andExpect(MockMvcResultMatchers.model().attribute("actividad",
						Matchers.hasProperty("descripcion", Matchers.is("Descripcion de prueba1"))))
				.andExpect(MockMvcResultMatchers.model().attribute("actividad",
						Matchers.hasProperty("titulo", Matchers.is("Prueba1"))))
				.andExpect(MockMvcResultMatchers.view().name("actividades/actividadesDetails"));
	}

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testInitUpdateActividadForm() throws Exception {
		mockMvc.perform(get("/actividades/{actividadId}/edit", TEST_A_ID))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(model().attributeExists("actividad"))
				.andExpect(model().attribute("actividad", hasProperty("descripcion", is("Descripcion de prueba1"))))
				.andExpect(model().attribute("actividad", hasProperty("titulo", is("Prueba1"))))
				.andExpect(view().name("actividades/createOrUpdateActividadForm"));
	}

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/actividades/{actividadId}/edit", TEST_A_ID)
						.param("titulo", "Prueba2").param("descripcion", "Prueba descrip")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fechaInicio", "2030/01/01")
						.param("horaInicio", "10:00").param("horaFin", "20:00"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/actividades/{actividadId}/edit", TEST_A_ID)
				.param("titulo", "Prueba").param("descripcion", "").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("fechaInicio", "2030/01/01").param("horaInicio", "10:00").param("horaFin", "20:00"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("actividad"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("actividades/createOrUpdateActividadForm"));
	}
	
	//los organizadores no deben poder acceder a la lista de actividades
	@WithMockUser(username = "organizador1")
	@Test
	void testListaActividadesComoOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/actividades"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los organizadores no deben poder acceder al show de actividades
	@WithMockUser(username = "organizador1")
	@Test
	void testShowActividadComoOrganizador() throws Exception {
		this.mockMvc
		.perform(MockMvcRequestBuilders.get("/actividades/{actividadId}", ActividadControllerE2ETest.TEST_A_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los ancianos no deben poder acceder a la edición de actividades
	@WithMockUser(username = "anciano1")
	@Test
	void testEditaActividadComoAnciano() throws Exception {
		this.mockMvc.perform(get("/actividades/{actividadId}/edit", TEST_A_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los organizadores no deben poder acceder a la edición de actividades
	@WithMockUser(username = "organizador1")
	@Test
	void testEditaActividadComoOrganizador() throws Exception {
		this.mockMvc.perform(get("/actividades/{actividadId}/edit", TEST_A_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los ancianos no deben poder acceder a la creación de actividades
	@WithMockUser(username = "anciano1")
	@Test
	void testCreaActividadComoAnciano() throws Exception {
		this.mockMvc.perform(get("/actividades/new"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los organizadores no deben poder acceder a la creación de actividades
	@WithMockUser(username = "organizador1")
	@Test
	void testCreaActividadComoOrganizador() throws Exception {
		this.mockMvc.perform(get("/actividades/new", TEST_A_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los ancianos no deben poder borrar actividades
	@WithMockUser(username = "anciano1")
	@Test
	void testBorraActividadComoAnciano() throws Exception {
		this.mockMvc.perform(get("/actividades/{actividadId}/delete", TEST_A_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//los organizadores no deben poder borrar actividades
	@WithMockUser(username = "organizador1")
	@Test
	void testBorraActividadComoOrganizador() throws Exception {
		this.mockMvc.perform(get("/actividades/{actividadId}/delete", TEST_A_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

}
