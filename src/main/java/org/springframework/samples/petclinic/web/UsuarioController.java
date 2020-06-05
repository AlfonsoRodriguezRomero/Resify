package org.springframework.samples.petclinic.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/selectCreateOrUpdateUsuarioForm";

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model) {
		return VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}

}
