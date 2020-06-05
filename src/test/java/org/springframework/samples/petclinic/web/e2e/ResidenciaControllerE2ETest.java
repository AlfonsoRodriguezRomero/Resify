
package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalTime;

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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*
 * @TestPropertySource(
 * locations = "classpath:application-mysql.properties")
 */
public class ResidenciaControllerE2ETest {

	private static final int	TEST_RESIDENCIA_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;

	private LocalTime			horini				= LocalTime.of(9, 0);
	private LocalTime			horfin				= LocalTime.of(20, 0);


	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessFindFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("residencias/residenciasDetails"));
	}

	@WithMockUser(username = "organizador1", authorities = {
		"organizador"
	})
	@Test
	void testProcessFindFormSuccessOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("residencias/residenciasList"));
	}

	@WithMockUser(username = "anciano1", authorities = {
		"anciano"
	})
	@Test
	void testProcessFindTopFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/top")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("residencias/residenciasList"));
	}

	@WithMockUser(username = "organizador1", authorities = {
		"organizador"
	})
	@Test
	void testProcessFindNoParticipantesFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/no-participantes")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("residencias/residenciasList"));
	}

	@WithMockUser(username = "manager3", authorities = {
		"manager"
	})
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("residencias/createOrUpdateResidenciaForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("residencia"));
	}

	@WithMockUser(username = "manager3", authorities = {
		"manager"
	})
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/residencias/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "residee").param("direccion", "direc").param("descripcion", "descp").param("aforo", "100").param("masInfo", "")
				.param("telefono", "674567123").param("correo", "resi@gmail.com").param("horaApertura", String.valueOf(this.horini)).param("horaCierre", String.valueOf(this.horfin)).param("edadMaxima", "82").param("aceptaDependenciaGrave", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "manager3", authorities = {
		"manager"
	})
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/residencias/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "residee").param("direccion", "direc").param("descripcion", "").param("aforo", "100").param("masInfo", "")
				.param("telefono", "674567123").param("correo", "resi@gmail.com").param("horaApertura", String.valueOf(this.horini)).param("horaCierre", String.valueOf(this.horfin)).param("edadMaxima", "82").param("aceptaDependenciaGrave", "true"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("residencia")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("residencias/createOrUpdateResidenciaForm"));
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	void testShowResidencia() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/{residenciaId}", ResidenciaControllerE2ETest.TEST_RESIDENCIA_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("residencia"))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("nombre", Matchers.is("Residencia 1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("direccion", Matchers.is("Direccion"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("descripcion", Matchers.is("Descripcion de prueba"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("aforo", Matchers.is(100))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("masInfo", Matchers.is("http://www.resi1.com"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("telefono", Matchers.is("987654321"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("correo", Matchers.is("residencia1@mail.es"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("edadMaxima", Matchers.is(70))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("aceptaDependenciaGrave", Matchers.is(false)))).andExpect(MockMvcResultMatchers.view().name("residencias/residenciasDetails"));
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	void testInitUpdateResidenciaForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/{residenciaId}/edit", ResidenciaControllerE2ETest.TEST_RESIDENCIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("residencia")).andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("nombre", Matchers.is("Residencia 1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("direccion", Matchers.is("Direccion"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("descripcion", Matchers.is("Descripcion de prueba"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("aforo", Matchers.is(100))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("masInfo", Matchers.is("http://www.resi1.com"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("telefono", Matchers.is("987654321"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("correo", Matchers.is("residencia1@mail.es"))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("edadMaxima", Matchers.is(70))))
			.andExpect(MockMvcResultMatchers.model().attribute("residencia", Matchers.hasProperty("aceptaDependenciaGrave", Matchers.is(false)))).andExpect(MockMvcResultMatchers.view().name("residencias/createOrUpdateResidenciaForm"));
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/residencias/{residenciaId}/edit", ResidenciaControllerE2ETest.TEST_RESIDENCIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "residee").param("direccion", "direc")
			.param("descripcion", "desc").param("aforo", "100").param("masInfo", "").param("telefono", "674567123").param("correo", "resi@gmail.com").param("horaApertura", String.valueOf(this.horini)).param("horaCierre", String.valueOf(this.horfin))
			.param("edadMaxima", "82").param("aceptaDependenciaGrave", "true")).andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/residencias/{residenciaId}/edit", ResidenciaControllerE2ETest.TEST_RESIDENCIA_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("nombre", "residee").param("direccion", "direc")
				.param("descripcion", "").param("aforo", "100").param("masInfo", "").param("telefono", "").param("correo", "resi@gmail.com").param("horaApertura", String.valueOf(this.horini)).param("horaCierre", String.valueOf(this.horfin))
				.param("edadMaxima", "82").param("aceptaDependenciaGrave", "false"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("residencia")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("residencias/createOrUpdateResidenciaForm"));
	}

	//no debe acceder al top residencias siendo manager
	@WithMockUser(username = "manager1")
	@Test
	void testProcessFindTopErrorManager() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/top")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	//no debe acceder al top residencias siendo organizador
	@WithMockUser(username = "organizador1")
	@Test
	void testProcessFindTopErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/top")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	//no debe acceder al ratio residencias siendo manager
		@WithMockUser(username = "manager1")
		@Test
		void testProcessFindRatioErrorManager() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/ratio")).andExpect(MockMvcResultMatchers.status().isForbidden());
		}

		//no debe acceder al ratio residencias siendo anciano
		@WithMockUser(username = "anciano1")
		@Test
		void testProcessFindRatioErrorAnciano() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/ratio")).andExpect(MockMvcResultMatchers.status().isForbidden());
		}	

	//no debe editar residencia siendo anciano
	@WithMockUser(username = "anciano1")
	@Test
	void testUpdateResidenciaErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/{residenciaId}/edit", ResidenciaControllerE2ETest.TEST_RESIDENCIA_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	//no debe editar residencia siendo organizador
	@WithMockUser(username = "organizador1")
	@Test
	void testUpdateResidenciaErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/{residenciaId}/edit", ResidenciaControllerE2ETest.TEST_RESIDENCIA_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	//no debe crear residencia siendo anciano
	@WithMockUser(username = "anciano1")
	@Test
	void testCrearResidenciaErrorAnciano() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	//no debe crear residencia siendo organizador
	@WithMockUser(username = "organizador1")
	@Test
	void testCrearResidenciaErrorOrganizador() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/residencias/new")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

}
