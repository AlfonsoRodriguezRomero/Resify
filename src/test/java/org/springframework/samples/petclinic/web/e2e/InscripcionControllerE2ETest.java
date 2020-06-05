package org.springframework.samples.petclinic.web.e2e;

import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
/*
 * @TestPropertySource(
 * locations = "classpath:application-mysql.properties")
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional

public class InscripcionControllerE2ETest {

	private static final int	TEST_INSCR_ID		= 1;
	private static final int	TEST_RES_ID		= 1;
	private Date					hoy						= new Date(System.currentTimeMillis() - 1);

	
	@Autowired
	private MockMvc				mockMvc;
	
	@WithMockUser(username = "manager1", authorities = {"manager"})	  
	  @Test 
	  void testProcessFindFormSuccessManager() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("inscripciones/inscripcionesList")); 
	  }
	  
	@WithMockUser(username = "joselitoanca", authorities = {"anciano"})	  
	  @Test 
	  void testProcessFindFormSuccessAnciano() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("inscripciones/inscripcionesList")); 
	  }
	  
	//un organizador no puede acceder al listado de inscripciones de una residencia
	@WithMockUser(username = "organizador1", authorities = {"organizador"})	  
	  @Test 
	  void testProcessFindFormSuccessErrorOrganizador() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones"))
	  .andExpect(MockMvcResultMatchers.status().isForbidden()); 
	  }
	
	@WithMockUser(username = "anciano1", authorities = {"anciano"})	  
	  @Test
	  void testInitCreationFormSuccesfull() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/new/{residenciaId}", TEST_INSCR_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("inscripciones/createOrUpdateInscripcionForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("inscripcion"));
	  }
	
	//un organizador no puede acceder al formulario para hacer una inscripción
	@WithMockUser(username = "organizador1", authorities = {"organizador"})	  
	  @Test
	  void testInitCreationFormErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/new/{residenciaId}", TEST_INSCR_ID))
		  .andExpect(MockMvcResultMatchers.status().isForbidden()); 
	  }	
	
	//un manager no puede acceder al formulario para hacer una inscripción
	@WithMockUser(username = "manager1", authorities = {"manager"})	  
	  @Test
	  void testInitCreationFormErrorManager() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/new/{residenciaId}", TEST_INSCR_ID))
		  .andExpect(MockMvcResultMatchers.status().isForbidden()); 
	  }
	 
	@WithMockUser(username = "anciano1", authorities = {"anciano"})	  
	  @Test
	  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
		void testProcessCreationFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/inscripciones/new/{residenciaId}", TEST_RES_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "dec")
						.param("estado", "pendiente").with(SecurityMockMvcRequestPostProcessors.csrf()))
						.andExpect(MockMvcResultMatchers.status().is3xxRedirection());		
	  }	
	
	  //un organizador no puede enviar inscripcion de una residencia
	@WithMockUser(username = "manager1", authorities = {"manager"})	  
	  @Test
		void testProcessCreationFormErrorManager() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/inscripciones/new/{residenciaId}", TEST_RES_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "dec")
						.param("estado", "pendiente")
						.param("fecha", String.valueOf(this.hoy)))
				  .andExpect(MockMvcResultMatchers.status().isForbidden()); 
	  }
 
	  //un organizador no puede enviar inscripcion de una residencia
	@WithMockUser(username = "organizador1", authorities = {"organizador"})	  
	  @Test
		void testProcessCreationFormErrorOrganizador() throws Exception {
			this.mockMvc
			.perform(MockMvcRequestBuilders.post("/inscripciones/new/{residenciaId}", TEST_INSCR_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "dec")
						.param("estado", "aceptada")
						.param("fecha", String.valueOf(this.hoy)))
			  .andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}
	
	@WithMockUser(username = "manager1", authorities = {"manager"})	  
	  @Test
	  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
		  void testInitUpdateForm() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/{inscripcionId}/edit", TEST_INSCR_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("inscripcion"))
			.andExpect(MockMvcResultMatchers.view().name("inscripciones/createOrUpdateInscripcionForm"));
		  }
	
	//un organizador no puede acceder al formulario para hacer una inscripción
	@WithMockUser(username = "organizador1", authorities = {"organizador"})	  
	  @Test
	  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
		  void testInitUpdateFormErrorOrganizador() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/{inscripcionId}/edit", TEST_INSCR_ID))
			  .andExpect(MockMvcResultMatchers.status().isForbidden()); 
		  }
	
	@WithMockUser(username = "manager1", authorities = {"manager"})	  
	  @Test
		void testProcessUpdateFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/inscripciones/{inscripcionId}/edit", TEST_INSCR_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("estado", "aceptada")
						.param("justificacion", "si"))
						.andExpect(MockMvcResultMatchers.status().isOk());
		}	
	
	@WithMockUser(username = "manager1", authorities = {"manager"})	  
		@Test
		void testShowInscripcionManager() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/{inscripcionId}", TEST_INSCR_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("inscripcion"))
				.andExpect(MockMvcResultMatchers.model().attribute("inscripcion", Matchers.hasProperty("estado", Matchers.is("pendiente"))))
				.andExpect(MockMvcResultMatchers.model().attribute("inscripcion", Matchers.hasProperty("declaracion", Matchers.is("Declaracion1"))))
				.andExpect(MockMvcResultMatchers.view().name("inscripciones/inscripcionesDetails"));
	}	
	
	@WithMockUser(username = "anciano1", authorities = {"anciano"})	  
	@Test
	void testShowInscripcionAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/{inscripcionId}", TEST_INSCR_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("inscripcion"))
			.andExpect(MockMvcResultMatchers.model().attribute("inscripcion", Matchers.hasProperty("estado", Matchers.is("pendiente"))))
			.andExpect(MockMvcResultMatchers.model().attribute("inscripcion", Matchers.hasProperty("declaracion", Matchers.is("Declaracion1"))))
			.andExpect(MockMvcResultMatchers.view().name("inscripciones/inscripcionesDetails"));
	}	
	
	@WithMockUser(username = "organizador1", authorities = {"organizador"})	  
	@Test
	void testShowInscripcionOrganizadorError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/inscripciones/{inscripcionId}", TEST_INSCR_ID))
		  .andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}	
}	  