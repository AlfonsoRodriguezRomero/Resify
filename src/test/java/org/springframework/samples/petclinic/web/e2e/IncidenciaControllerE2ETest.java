
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
 * @TestPropertySource(
 * locations = "classpath:application-mysql.properties")
 */
public class IncidenciaControllerE2ETest {

	private static final int	TEST_INC_ID		= 1;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("incidencias/incidenciasList"));
	}
	
	//un organizador no puede listar las incidencias
	@WithMockUser(username = "organizador1", authorities = {
			"organizador"
		})
		@Test
		void testProcessFindFormErrorOrganizador() throws Exception {

			this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias"))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
		}
	
	//un anciano no puede listar las incidencias
		@WithMockUser(username = "anciano1", authorities = {
				"anciano"
			})
			@Test
			void testProcessFindFormErrorAnciano() throws Exception {

				this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
			}
	
	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("incidencias/createOrUpdateIncidenciaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("incidencia"));
	}
	
	//un anciano no puede acceder al formulario de crear incidencias
	@WithMockUser(username = "anciano1", authorities = {
			"anciano"
		})
		@Test
		void testInitCreationFormErrorAnciano() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
		}
	
	//un organizador no puede acceder al formulario de crear incidencias
		@WithMockUser(username = "organizador1", authorities = {
				"organizador"
			})
			@Test
			void testInitCreationFormErrorOrganizador() throws Exception {
				this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
			}
		
	
	//un manager sin residencia registrada intenta registrar una incidencia
	@WithMockUser(username = "manager3", authorities = {
			"manager"
		})
		@Test
		void testInitCreationFormErrorManagerSinResidencia() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias/new"))
			.andExpect(MockMvcResultMatchers.view().name("exception")); 
		}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/incidencias/new").param("titulo", "Prueba").param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	//un anciano no puede enviar formulario de incidencia
	@WithMockUser(username = "anciano1", authorities = {
			"anciano"
		})
		@Test
		void testProcessCreationFormErrorAnciano() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.post("/incidencias/new").param("titulo", "Prueba").param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01"))
			.andExpect(MockMvcResultMatchers.status().isForbidden());		}
	
	//un organizador no puede enviar formulario de incidencia
	@WithMockUser(username = "organizador1", authorities = {
			"organizador"
		})
		@Test
		void testProcessCreationFormErrorOrganizador() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.post("/incidencias/new").param("titulo", "Prueba").param("descripcion", "Prueba descrip").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01"))
			.andExpect(MockMvcResultMatchers.status().isForbidden());		}
	
	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/incidencias/new").param("titulo", "Prueba").with(SecurityMockMvcRequestPostProcessors.csrf()).param("fecha", "2020/01/01").param("descripcion", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("incidencia")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("incidencias/createOrUpdateIncidenciaForm"));
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testShowBuenaAccion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias/{incidenciaId}", IncidenciaControllerE2ETest.TEST_INC_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("incidencia"))
			.andExpect(MockMvcResultMatchers.model().attribute("incidencia", Matchers.hasProperty("descripcion", Matchers.is("Descripcion de incidencia1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("incidencia", Matchers.hasProperty("titulo", Matchers.is("titulo1")))).andExpect(MockMvcResultMatchers.view().name("incidencias/incidenciasDetails"));
	}

	//un organizador no puede acceder a una incidencia
	@WithMockUser(username = "organizador1", authorities = {
			"organizador"
		})
		@Test
		void testShowBuenaAccionOrganizador() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias/{incidenciaId}", IncidenciaControllerE2ETest.TEST_INC_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());	
		}
	
	//un anciano no puede acceder a una incidencia
	@WithMockUser(username = "anciano1", authorities = {
			"anciano"
		})
		@Test
		void testShowBuenaAccionAnciano() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/incidencias/{incidenciaId}", IncidenciaControllerE2ETest.TEST_INC_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());	
		}
}
