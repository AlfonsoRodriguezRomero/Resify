
package org.springframework.samples.petclinic.web.e2e;

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
/*
 * @TestPropertySource( locations = "classpath:application-mysql.properties")
 */
public class QuejaControllerE2ETest {

	private static final int TEST_MANAGER_ID = 1;
	private static final int TEST_QUEJA_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("quejas/quejasList"));
	}

	// No debe poder listar quejas siendo anciano
	@WithMockUser(username = "anciano1")
	@Test
	void testProcessFindFormErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// No debe poder listar quejas siendo organizador
	@WithMockUser(username = "organizador1")
	@Test
	void testProcessFindFormErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = "anciano3", authorities = { "anciano" })
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("quejas/createOrUpdateQuejaForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("queja"));
	}

	@WithMockUser(username = "anciano3", authorities = { "anciano" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/quejas/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("titulo", "Prueba").param("descripcion", "Prueba descrip").param("anonimo", "true"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "anciano3", authorities = { "anciano" })
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/quejas/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("titulo", "").param("descripcion", "").param("anonimo", "false"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("queja", "titulo", "descripcion"))
				.andExpect(MockMvcResultMatchers.view().name("quejas/createOrUpdateQuejaForm"));
	}

	// No debe poder crear queja siendo manager
	@WithMockUser(username = "manager1")
	@Test
	void testInitCreationFormErrorManager() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/new"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// No debe poder crear queja siendo organizador
	@WithMockUser(username = "organizador1")
	@Test
	void testInitCreationFormErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/new"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(username = "manager1", authorities = { "manager" })
	@Test
	void testShowQueja() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/{quejaId}", QuejaControllerE2ETest.TEST_QUEJA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("queja"))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("descripcion", Matchers.is("Descripcion Prueba 1"))))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("titulo", Matchers.is("Titulo Prueba 1"))))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("anonimo", Matchers.is(false))))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("anciano", Matchers.hasProperty("nombre", Matchers.is("Rosa")))))
				.andExpect(MockMvcResultMatchers.model().attribute("queja",
						Matchers.hasProperty("anciano", Matchers.hasProperty("apellidos", Matchers.is("Gonzalez")))))
				.andExpect(MockMvcResultMatchers.view().name("quejas/quejasDetails"));
	}

	// No debe poder crear queja siendo anciano
	@WithMockUser(username = "anciano1")
	@Test
	void testShowQuejaErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/{quejaId}", QuejaControllerE2ETest.TEST_QUEJA_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// No debe poder crear queja siendo organizador
	@WithMockUser(username = "organizador1")
	@Test
	void testShowQuejaErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/quejas/{quejaId}", QuejaControllerE2ETest.TEST_QUEJA_ID))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

}
