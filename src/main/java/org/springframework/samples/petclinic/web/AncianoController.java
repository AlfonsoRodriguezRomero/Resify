package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ancianos")
public class AncianoController {

	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/createOrUpdateUsuarioForm";

	private final AncianoService ancianoService;

	private final AuthoritiesService authService;

	@Autowired
	public AncianoController(AncianoService ancianoService, AuthoritiesService authoritiesService) {
		this.ancianoService = ancianoService;
		this.authService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		Anciano anciano = new Anciano();
		model.put("anciano", anciano);
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Anciano anciano, BindingResult result, Map<String, Object> model) {
		if (result.hasErrors()) {
			model.put("anciano", anciano);
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		} else {
			authService.saveAuthorities(anciano.getUser().getUsername(), "anciano");
			ancianoService.saveAnciano(anciano);
			return "redirect:/";
		}
	}
}