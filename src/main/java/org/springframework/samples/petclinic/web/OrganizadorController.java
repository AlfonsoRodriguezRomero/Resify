package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organizadores")
public class OrganizadorController {

	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/createOrUpdateUsuarioForm";

	private final OrganizadorService organizadorService;

	private final AuthoritiesService authService;

	@Autowired
	public OrganizadorController(OrganizadorService organizadorService, AuthoritiesService authoritiesService) {
		this.organizadorService = organizadorService;
		this.authService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		Organizador organizador = new Organizador();
		model.put("organizador", organizador);
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Organizador organizador, BindingResult result, Map<String, Object> model) {
		if (result.hasErrors()) {
			model.put("organizador", organizador);
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		} else {
			authService.saveAuthorities(organizador.getUser().getUsername(), "organizador");
			organizadorService.saveOrganizador(organizador);
			return "redirect:/";
		}
	}
}