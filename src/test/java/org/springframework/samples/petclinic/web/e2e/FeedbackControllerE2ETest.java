
package org.springframework.samples.petclinic.web.e2e;

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

public class FeedbackControllerE2ETest {

	private static final int	TEST_EX_ID		= 8;
	private static final int	TEST_FE_ID		= 1;
	private static final int	TEST_EX_NOFINAL_ID		= 9;
	private static final int	TEST_EX_NOCADUCADA_ID		= 10;
	private static final int	TEST_EX_NOACEPTADA_ID		= 1;

	
	@Autowired
	private MockMvc				mockMvc;
	
	@WithMockUser(username = "organizador1", authorities = {"organizador"})	  
	@Test 
	  void testProcessFindFormSuccessOrganizador() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks"))
	  .andExpect(MockMvcResultMatchers.status().isOk())
	  .andExpect(MockMvcResultMatchers.view().name("feedbacks/feedbackList")); 
	  }
	  
	  //manager intenta acceder al listado de feedbacks
	@WithMockUser(username = "manager1", authorities = {"manager"})	  
	  @Test 
	  void testProcessFindFormErrorManager() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks"))
	  .andExpect(MockMvcResultMatchers.view().name("exception")); 
	  }
	
	//anciano intenta acceder al listado de feedbacks
	@WithMockUser(username = "anciano1", authorities = {"anciano"})	  
	  @Test 
	  void testProcessFindFormErrorAnciano() throws Exception {
	  this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks"))
	  .andExpect(MockMvcResultMatchers.status().isForbidden()); 
	  }	  
	
	@WithMockUser(username = "manager1", authorities = {"manager"})	  
	  @Test
	  void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("feedbacks/createOrUpdateFeedbackForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("feedback"));
	  }
	  
	  //no debe poder acceder al form de crear un feedback un manager distinto al que hizo la petición de excursión aceptada
	@WithMockUser(username = "manager2", authorities = {"manager"})	  
	  @Test
	  void testInitCreationFormErrorManagerIncorrecto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	  //no debe poder acceder al form de crear un feedback un organizador
	  @WithMockUser(username = "organizador1", authorities = {"organizador"})	  
	  @Test
	  void testInitCreationFormErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}

	  //no debe poder acceder al form de crear un feedback un anciano
	  @WithMockUser(username = "anciano1", authorities = {"anciano"})
	  @Test
	  void testInitCreationFormErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}  
	
	  @WithMockUser(username = "manager1", authorities = {"manager"})
	  @Test
		void testProcessCreationFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", TEST_EX_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.status()
						.isOk()).andExpect(MockMvcResultMatchers.view()
						.name("feedbacks/createOrUpdateFeedbackForm"));
		}
	  
	  //no debe poder enviar el form de crear un feedback un manager distinto al que hizo la petición de excursión aceptada
	  @WithMockUser(username = "manager2")
	  @Test
		void testProcessCreationFormErrorManagerIncorrecto() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", TEST_EX_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}	
	//no debe poder acceder al form de crear un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "manager1", authorities = {"manager"})
	  @Test
	  void testInitCreationFormErrorNoFinalModeExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOFINAL_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	  }
	  
	  //no debe poder acceder al form de crear un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "manager2")
	  @Test
	  void testInitCreationFormErrorManagerIncorrectoNoFinalModeExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOFINAL_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "manager1", authorities = {"manager"})
	  @Test
		void testProcessCreationFormErrorNoFinalModeExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOFINAL_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));

		}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "manager2")
	  @Test
		void testProcessCreationFormErrorManagerIncorrectoNoFinalModeExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOFINAL_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}	  
	
	  //no debe poder acceder al form de crear un feedback para una excursión que no está caducada
	  @WithMockUser(username = "manager1", authorities = {"manager"})
	  @Test
	  void testInitCreationFormErrorNoCaducadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOCADUCADA_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	  }
	  
	  //no debe poder acceder al form de crear un feedback para una excursión que no está caducada
	  @WithMockUser(username = "manager2")
	  @Test
	  void testInitCreationFormErrorManagerIncorrectoNoCaducadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOCADUCADA_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está caducada
	  @WithMockUser(username = "manager1", authorities = {"manager"})
	  @Test
		void testProcessCreationFormErrorNoCaducadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOCADUCADA_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está caducada
	  @WithMockUser(username = "manager2")
	  @Test
		void testProcessCreationFormErrorManagerIncorrectoNoCaducadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOCADUCADA_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}	  	  
	  
	  //no debe poder acceder al form de crear un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "manager1", authorities = {"manager"})
	  @Test
	  void testInitCreationFormErrorNoAceptadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOACEPTADA_ID))
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	  }
	  
	  //no debe poder acceder al form de crear un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "manager2")
	  @Test
	  void testInitCreationFormErrorManagerIncorrectoNoAceptadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOACEPTADA_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "manager1", authorities = {"manager"})
	  @Test
		void testProcessCreationFormErrorNoAceptadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOACEPTADA_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	  
	  //no debe poder enviar el form de crear un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "manager2")
	  @Test
		void testProcessCreationFormErrorManagerIncorrectoNoAceptadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/excursiones/{excursionId}/feedbacks/new", TEST_EX_NOACEPTADA_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		} 
	  
	  @WithMockUser(username = "organizador1", authorities = {"organizador"})	  
	  @Test
	  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
		  void testInitUpdateForm() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", TEST_FE_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("feedback"))
			.andExpect(MockMvcResultMatchers.model().attribute("feedback", Matchers.hasProperty("descripcion", Matchers.is("desc1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("feedback", Matchers.hasProperty("valoracion", Matchers.is(2))))
			.andExpect(MockMvcResultMatchers.model().attribute("feedback", Matchers.hasProperty("descartaFeedback", Matchers.is(false))))
			.andExpect(MockMvcResultMatchers.view().name("feedbacks/createOrUpdateFeedbackForm"));
		  }
	  
	  
	  //no debe poder acceder al form de actualizar un feedback un organizador distinto al que recibió el feedback
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
	  void testInitUpdateFormErrorOrganizadorIncorrecto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", TEST_FE_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}
	  
	  //no debe poder acceder al form de actualizar un feedback un manager
	  @WithMockUser(username = "manager1", authorities = {"manager"})
	  @Test
	  void testInitUpdateFormErrorManager() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", TEST_FE_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}

	  //no debe poder acceder al form de actualizar un feedback un anciano
	  @WithMockUser(username = "anciano1", authorities = {"anciano"})
	  @Test
	  void testInitUpdateFormErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", TEST_FE_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}	
	  
	  @WithMockUser(username = "organizador1", authorities = {"organizador"})	  
	  @Test
		void testProcessUpdateFormSuccess() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", TEST_FE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
						.andExpect(MockMvcResultMatchers.status().isOk());
		}
	  
	  //no debe poder enviar el form de update un feedback un organizador distinto al que recibió el feedback
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
		void testProcessUpdateFormErrorOrganizadorIncorrecto() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", TEST_FE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}	  
	  
	  //no debe poder enviar el form de update un feedback un anciano
	  @WithMockUser(username = "anciano1", authorities = {"anciano"})
	  @Test
		void testProcessUpdateFormErrorAnciano() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", TEST_FE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}	  

	  //no debe poder acceder al form de update un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
	  void testInitUpdateFormErrorManagerIncorrectoNoFinalModeExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", TEST_FE_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}
	  
	  //no debe poder enviar el form de update un feedback para una excursión que no está en finalMode
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
		void testProcessUpdateFormErrorManagerIncorrectoNoFinalModeExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", TEST_FE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}	  
	  
	  //no debe poder acceder al form de update un feedback para una excursión que no está caducada
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
	  void testInitUpdateFormErrorManagerIncorrectoNoCaducadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", TEST_FE_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}
	  
	  //no debe poder enviar el form de update un feedback para una excursión que no está caducada
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
		void testProcessUpdateFormErrorManagerIncorrectoNoCaducadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", TEST_FE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}	  	  
	  
	  //no debe poder acceder al form de update un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "organizadorIncorrecto")
	  @Test
	  void testInitUpdateFormErrorManagerIncorrectoNoAceptadaExcursion() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/feedbacks/{feedbackId}/edit", TEST_FE_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden()); 
	}
	 
	  //no debe poder enviar el form de update un feedback para una excursión que no está aceptada
	  @WithMockUser(username = "managerIncorrecto")
	  @Test
		void testProcessUpdateFormErrorManagerIncorrectoNoAceptadaExcursion() throws Exception {
			this.mockMvc
				.perform(MockMvcRequestBuilders.post("/feedbacks/{feedbackId}/edit", TEST_FE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("declaracion", "Descripcion de prueba")
						.param("valoracion", "2")
						.param("descartaFeedback", "false"))
				.andExpect(MockMvcResultMatchers.status().isForbidden()); 
		}
	 	  
}	  