package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/managers")
public class ManagerController {

	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/createOrUpdateUsuarioForm";

	private final ManagerService managerService;

	private final AuthoritiesService authService;

	@Autowired
	public ManagerController(ManagerService managerService, AuthoritiesService authoritiesService) {
		this.managerService = managerService;
		this.authService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		Manager manager = new Manager();
		model.put("manager", manager);
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Manager manager, BindingResult result, Map<String, Object> model) {
		if (result.hasErrors()) {
			model.put("manager", manager);
			return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
		} else {
			authService.saveAuthorities(manager.getUser().getUsername(), "manager");
			managerService.saveManager(manager);
			return "redirect:/";
		}
	}
}