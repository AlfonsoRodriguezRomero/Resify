
package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.Instant;
import java.util.Date;

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
/*
 * @TestPropertySource(
 * locations = "classpath:application-mysql.properties")
 */
public class PeticionExcursionControllerE2ETest {

	private static final int	TEST_EXCURSION_ID = 5;
	private static final int	TEST_EXCURSION_PASADA_ID = 1;
	private static final int	TEST_PETICION_EXCURSION_ID = 3;

	

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessFindFormSuccessManager() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/peticiones-excursion"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("peticionesExcursion/peticionExcursionList"));
	}
	
	@WithMockUser(username = "organizador1", authorities = {
			"organizador"
		})
		@Test
		void testProcessFindFormSuccessOrganizador() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/peticiones-excursion"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("peticionesExcursion/peticionExcursionList"));
		}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/peticiones-excursion/new", TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("peticionesExcursion/createOrUpdatePeticionExcursionForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("peticionExcursion"));
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/peticiones-excursion/new",TEST_EXCURSION_ID).with(csrf())
				.param("declaracion", "Prueba declaracion")
				.param("estado", "pendiente"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessCreationFormHasErrorsBlank() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/peticiones-excursion/new", TEST_EXCURSION_ID).with(csrf())
				.param("declaracion", "")
				.param("estado", "pendiente"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("peticionExcursion", "declaracion"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("peticionesExcursion/createOrUpdatePeticionExcursionForm"));
	}
	
	@WithMockUser(username = "manager2", authorities = {
			"manager"
		})
	@Test
	void testProcessCreationFormHasErrorsPastExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/peticiones-excursion/new", TEST_EXCURSION_PASADA_ID).with(csrf())
				.param("declaracion", "Declaracion")
				.param("estado", "pendiente"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	@WithMockUser(username = "organizador1", authorities = {
			"organizador"
		})
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/peticiones-excursion/{peticionExcursionId}/edit", TEST_PETICION_EXCURSION_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("peticionExcursion"))
			.andExpect(MockMvcResultMatchers.view().name("peticionesExcursion/createOrUpdatePeticionExcursionForm"));
	}
	
	@WithMockUser(username = "organizador1", authorities = {
			"organizador"
		})
	@Test
	void testProcessUpdateFormSuccessAccept() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/peticiones-excursion/{peticionExcursionId}/edit", TEST_PETICION_EXCURSION_ID).with(csrf())
			.param("id", String.valueOf(TEST_PETICION_EXCURSION_ID)).with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("fecha", String.valueOf(Date.from(Instant.now().minusMillis(1)))).param("declaracion", "declaracion test")
			.param("estado", "aceptada"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/peticiones-excursion/"));
	}
	
	@WithMockUser(username = "organizador1", authorities = {
			"organizador"})
	@Test
	void testProcessUpdateFormErrorRejectWithoutJustification() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/peticiones-excursion/{peticionExcursionId}/edit", TEST_PETICION_EXCURSION_ID).with(csrf())
				.param("id", String.valueOf(TEST_PETICION_EXCURSION_ID))
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", String.valueOf(Date.from(Instant.now().minusMillis(1))))
				.param("declaracion", "declaracion test")
				.param("estado", "rechazada").param("justificacion", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("peticionExcursion"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("peticionExcursion", "justificacion"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("peticionesExcursion/createOrUpdatePeticionExcursionForm"));
	}
	
	//no debe acceder a la creación de peticiones siendo anciano
	@WithMockUser(username = "anciano1", authorities = "anciano")
	@Test
	void testInitCreationFormComoAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/peticiones-excursion/new", TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//no debe acceder a la creación de peticiones siendo anciano
	@WithMockUser(username = "organizador1", authorities = "organizador")
	@Test
	void testInitCreationFormComoOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/peticiones-excursion/new", TEST_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//no debe acceder a la creación de peticiones siendo anciano
	@WithMockUser(username = "anciano1", authorities = "anciano")
	@Test
	void testInitUpdateFormComoAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/peticiones-excursion/{peticionExcursionId}/edit", TEST_PETICION_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//no debe acceder a la creación de peticiones siendo manager
	@WithMockUser(username = "manager1", authorities = "manager")
	@Test
	void testInitUpdateFormComoManager() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/peticiones-excursion/{peticionExcursionId}/edit", TEST_PETICION_EXCURSION_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	

}
